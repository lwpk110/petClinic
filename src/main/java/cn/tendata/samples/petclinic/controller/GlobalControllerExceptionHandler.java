package cn.tendata.samples.petclinic.controller;

import cn.tendata.samples.petclinic.core.BasicErrorCodeException;
import cn.tendata.samples.petclinic.core.DefaultMessageSource;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * <h2> 全局异常捕获处理</h2>
 * <ul>
 *     <li>扩展了一些错误的处理</li>
 *     <li>重写了 {@link #handleBindException(BindException, HttpHeaders, HttpStatus, WebRequest)},{@link #handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}</li>
 *     <li>异常国际化</li>
 *     <li>统一异常返回</li>
 * </ul>
 *
 * @see ResponseEntityExceptionHandler
 * @see ExceptionHandlerExceptionResolver
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler implements
	MessageSourceAware {

	public static final String INVALID_PARAMETER = "InvalidParameter";
	public static final String MISSING_PARAMETER = "MissingParameter";
	public static final String OPERATION_CONFLICT = "OperationConflict";
	public static final String INTERNAL_ERROR = "InternalError";
	private static final Logger LOGGER = LoggerFactory.getLogger(
		GlobalControllerExceptionHandler.class);
	private MessageSourceAccessor messages = DefaultMessageSource.getAccessor();

	/**
	 *
	 * 数据绑定异常处理 1。
	 *
	 * <p>一般和{@link Valid} + {@link RequestParam} 注解结合使用。当controller 层 query 请求和 对应的 dto 对象进行绑定，
	 * 出现验证错误时，{@link BindResult} 对象会使用这些错误信息进行填充，返回一个 {@link BindException} 异常</p>
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleValidationException(ex.getBindingResult(), status);
	}

	/**
	 *
	 * 数据绑定异常处理 2
	 *
	 * <p>一般和{@link Validated} + {@link RequestBody} 注解结合使用。当controller 层 请求 json 数据和 对应的 dto 对象进行绑定，
	 * 出现验证错误时，{@link BindResult} 对象会使用这些错误信息进行填充，返回一个 {@link MethodArgumentNotValidException} 异常</p>
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleValidationException(ex.getBindingResult(), status);
	}

	private ResponseEntity<Object> handleValidationException(BindingResult result,
		HttpStatus status) {
		Map<String, Object> errorAttributes = createErrorAttributes(status, INVALID_PARAMETER);
		if (result != null) {
			if (result.hasGlobalErrors()) {
				List<Error> globalErrors = new ArrayList<>(8);
				for (ObjectError err : result.getGlobalErrors()) {
					Error error = new Error();
					error.message = messages.getMessage(err.getCode(), err.getDefaultMessage());
					globalErrors.add(error);
				}
				errorAttributes.put("errors", globalErrors);
			}
			if (result.hasFieldErrors()) {
				List<Error> fieldErrors = new ArrayList<>(8);
				for (FieldError err : result.getFieldErrors()) {
					Error error = new Error();
					error.field = err.getField();
					error.rejected = err.getRejectedValue();
					error.message = messages.getMessage(err.getCode(), err.getDefaultMessage());
					fieldErrors.add(error);
					errorAttributes.put("fieldErrors", fieldErrors);
				}
			}
			errorAttributes.put("message",
				messages.getMessage("error.ValidationError", "Validation failed for object='"
					+ result.getObjectName() + "'. Error count: " + result.getErrorCount()));
		}
		return new ResponseEntity<>(errorAttributes, status);
	}

	/**
	 * 一般当注解 {@link RequestParam} 标注的参数（“require” 属性默认 “true”）没有传值时会抛出此异常。
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
		MissingServletRequestParameterException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> errorAttributes = createErrorAttributes(status, MISSING_PARAMETER,
			ex.getLocalizedMessage());
		return new ResponseEntity<>(errorAttributes, status);
	}


	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> errorAttributes = createErrorAttributes(status, resolveErrorCode(ex),
			ex.getLocalizedMessage());
		return new ResponseEntity<>(errorAttributes, headers, status);
	}

	/**
	 *
	 * 数据绑定异常处理 3
	 *
	 * <p>一般和{@link Validated} + {@link RequestParam} 注解结合使用。当controller 层 请求 query 数据和 对应的 dto 对象进行绑定，
	 * 出现验证错误时，返回一个 {@link ConstraintViolationException} 异常</p>
	 *
	 */

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Map<String, Object> errorAttributes = createErrorAttributes(status, INVALID_PARAMETER,
			ex.getLocalizedMessage());
		List<Error> errors = new ArrayList<Error>(8);
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			Error error = new Error();
			error.message = violation.getMessage();
			errors.add(error);
		}
		errorAttributes.put("errors", errors);
		return new ResponseEntity<>(errorAttributes, status);
	}

	/**
	 *方法参数的类型和请求参数类型不匹配。一般出现在 uri 参数中。例如传入一个数字，使用一个对象接收。
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
		MethodArgumentTypeMismatchException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Map<String, Object> errorAttributes = createErrorAttributes(status, INVALID_PARAMETER,
			ex.getLocalizedMessage());
		return new ResponseEntity<>(errorAttributes, status);
	}

	/**
	 *
	 * 自定义异常处理。{@link BasicErrorCodeException}，支持异常国际化。
	 *
	 */
	@ExceptionHandler(BasicErrorCodeException.class)
	public ResponseEntity<Object> handleErrorCodeException(BasicErrorCodeException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Map<String, Object> errorAttributes = createErrorAttributes(status, ex.getErrorCode(),
			messages.getMessage("error." + ex.getErrorCode(), ex.getArgs(),
				ex.getLocalizedMessage()));
		return new ResponseEntity<>(errorAttributes, status);
	}

	/**
	 * 403 异常处理
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex,
		HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpStatus status = HttpStatus.FORBIDDEN;
		logException(ex, request, status);
		Map<String, Object> errorAttributes = createErrorAttributes(status,
			"Forbidden." + resolveErrorCode(ex),
			ex.getLocalizedMessage());
		return new ResponseEntity<>(errorAttributes, status);
	}

	/**
	 * 资源数据为空异常处理，直接返回 404
	 */
	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleNotFoundException() {
	}

	/**
	 * 乐观锁异常， 完整性约束异常处理
	 */
	@ExceptionHandler({OptimisticLockingFailureException.class,
		DataIntegrityViolationException.class})
	public ResponseEntity<Object> handleConflictException(Exception ex,
		HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpStatus status = HttpStatus.CONFLICT;
		logException(ex, request, status);
		Map<String, Object> errorAttributes = createErrorAttributes(status, OPERATION_CONFLICT,
			ex.getLocalizedMessage());
		return new ResponseEntity<>(errorAttributes, status);
	}

	/**
	 * 其它未声明的异常处理。-500
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaughtException(Exception ex,
		HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		logException(ex, request, status);
		Map<String, Object> errorAttributes = createErrorAttributes(status, INTERNAL_ERROR,
			messages.getMessage("error.InternalServerError", "Server error"));
		return new ResponseEntity<>(errorAttributes, status);
	}

	private String resolveErrorCode(Throwable ex) {
		return ex.getClass().getSimpleName().replace("Exception", "");
	}

	private Map<String, Object> createErrorAttributes(HttpStatus status, String code) {
		return createErrorAttributes(status, code, null);
	}

	private Map<String, Object> createErrorAttributes(HttpStatus status, String code,
		String message) {
		Map<String, Object> errorAttributes = new LinkedHashMap<>();
		errorAttributes.put("status", status.value());
		errorAttributes.put("code", code);
		errorAttributes.put("message", message);
		errorAttributes.put("timestamp", new Date());
		return errorAttributes;
	}

	private void logException(Throwable ex, HttpServletRequest request, HttpStatus status) {
		if (LOGGER.isErrorEnabled() && status.value() >= 500 || LOGGER.isInfoEnabled()) {
			Marker marker = MarkerFactory.getMarker(ex.getClass().getName());
			String uri = request.getRequestURI();
			if (request.getQueryString() != null) {
				uri += '?' + request.getQueryString();
			}
			String msg = String.format("%s %s ~> %s", request.getMethod(), uri, status);
			if (status.value() >= 500) {
				LOGGER.error(marker, msg, ex);
			} else if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(marker, msg, ex);
			} else {
				LOGGER.info(marker, msg);
			}
		}
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	@JsonInclude(Include.NON_EMPTY)
	static class Error {

		public String field;
		public Object rejected;
		public String message;
	}
}
