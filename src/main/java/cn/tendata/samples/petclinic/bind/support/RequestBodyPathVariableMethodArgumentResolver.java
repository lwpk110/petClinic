package cn.tendata.samples.petclinic.bind.support;

import cn.tendata.samples.petclinic.bind.annotation.RequestBodyPathVariable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

public class RequestBodyPathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver,
	ApplicationContextAware {

	private ApplicationContext applicationContext;

	private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = null;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestBodyPathVariable.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		parameter = parameter.nestedIfOptional();
		// Get attribute name from URL
		String attributeName = parameter.getParameterAnnotation(RequestBodyPathVariable.class)
			.value();

		// Get value for the URL attribute
		String value = getRequestValueForAttribute(attributeName, webRequest);

		// Get existing resource
		Object target = createAttributeFromRequestValue(value, attributeName, parameter,
			binderFactory, webRequest);

		if (target != null) {
			// Get the request body
			Method fakeMethod = ClassUtils.getMethod(this.getClass(), "fakeMethod", Map.class);
			MethodParameter fakeParameter = new MethodParameter(fakeMethod, 0);
			Map<?, ?> fakeMapArg = (Map<?, ?>) getRequestResponseBodyMethodProcessor().resolveArgument(
				fakeParameter, mavContainer, webRequest, binderFactory);

			String name = Conventions.getVariableNameForParameter(parameter);
			WebDataBinder binder = binderFactory.createBinder(webRequest, target, name);

			// Merge the request body into the existing resource
			BeanWrapper beanWrapper = createBeanWrapper(target, binder.getConversionService());
			setPropertyValues(beanWrapper, fakeMapArg);

			validateIfApplicable(binder, parameter);
			if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder,
				parameter)) {
				throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
			}
		}
		return target;
	}

	@ResponseBody
	public void fakeMethod(@RequestBody Map<String, Object> map) {

	}

	private RequestResponseBodyMethodProcessor getRequestResponseBodyMethodProcessor() {
		if (requestResponseBodyMethodProcessor == null) {
			RequestMappingHandlerAdapter requestMappingHandlerAdapter = applicationContext.getBean(
				RequestMappingHandlerAdapter.class);
			List<HttpMessageConverter<?>> messageConverters = requestMappingHandlerAdapter.getMessageConverters();
			requestResponseBodyMethodProcessor = new RequestResponseBodyMethodProcessor(
				messageConverters);
		}
		return requestResponseBodyMethodProcessor;
	}

	/**
	 * Copied from {@see org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor#getRequestValueForAttribute)
	 *
	 * @param attributeName
	 * @param request
	 * @return
	 */
	private String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
		Map<String, String> variables = getUriTemplateVariables(request);
		if (StringUtils.hasText(variables.get(attributeName))) {
			return variables.get(attributeName);
		} else if (StringUtils.hasText(request.getParameter(attributeName))) {
			return request.getParameter(attributeName);
		} else {
			return null;
		}
	}

	/**
	 * Copied from {@see org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor#getRequestValueForAttribute)
	 *
	 * @param attributeName
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
		Map<String, String> variables = (Map<String, String>) request.getAttribute(
			HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
			RequestAttributes.SCOPE_REQUEST);
		return (variables != null) ? variables : Collections.emptyMap();
	}

	private Object createAttributeFromRequestValue(String sourceValue, String attributeName,
		MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request)
		throws Exception {

		DataBinder binder = binderFactory.createBinder(request, null, attributeName);
		ConversionService conversionService = binder.getConversionService();
		if (conversionService != null) {
			TypeDescriptor source = TypeDescriptor.valueOf(String.class);
			TypeDescriptor target = new TypeDescriptor(parameter);
			if (conversionService.canConvert(source, target)) {
				return binder.convertIfNecessary(sourceValue, parameter.getParameterType(),
					parameter);
			}
		}
		return null;
	}

	private BeanWrapper createBeanWrapper(Object target, ConversionService conversionService) {
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(target);
		beanWrapper.setAutoGrowNestedPaths(true);
		beanWrapper.setConversionService(conversionService);
		return beanWrapper;
	}

	private void setPropertyValues(BeanWrapper bean, Map<?, ?> map) {
		for (Entry<?, ?> entry : map.entrySet()) {
			String propertyName = entry.getKey().toString();
			Object value = entry.getValue();
			if (bean.isWritableProperty(propertyName)) {
				if (value != null && Map.class.isAssignableFrom(value.getClass())) {
					Object nestedTarget = bean.getPropertyValue(propertyName);
					if (nestedTarget == null) {
						nestedTarget = BeanUtils.instantiateClass(
							bean.getPropertyType(propertyName));
						bean.setPropertyValue(propertyName, nestedTarget);
					}
					BeanWrapper nestedBean = createBeanWrapper(nestedTarget,
						bean.getConversionService());
					setPropertyValues(nestedBean, (Map<?, ?>) value);
				} else {
					int pos = PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(
						propertyName);
					if (pos > -1) {
						propertyName = propertyName.substring(0, pos);
					}
					Class<?> propertyType = bean.getPropertyType(propertyName);
					if (value != null && bean.getConversionService()
						.canConvert(value.getClass(), propertyType)) {
						value = bean.convertIfNecessary(value, propertyType);
					}
					bean.setPropertyValue(propertyName, value);
				}
			}
		}
	}

	/**
	 * Validate the binding target if applicable.
	 * <p>The default implementation checks for {@code @javax.validation.Valid},
	 * Spring's {@link Validated}, and custom annotations whose name starts with "Valid".
	 *
	 * @param binder    the DataBinder to be used
	 * @param parameter the method parameter descriptor
	 */
	private void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation ann : annotations) {
			Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
			if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = (validatedAnn != null ? validatedAnn.value()
					: AnnotationUtils.getValue(ann));
				Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints
					: new Object[]{hints});
				binder.validate(validationHints);
				break;
			}
		}
	}

	/**
	 * Whether to raise a fatal bind exception on validation errors.
	 *
	 * @param binder    the data binder used to perform data binding
	 * @param parameter the method parameter descriptor
	 * @return {@code true} if the next method argument is not of type {@link Errors}
	 */
	private boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(
			paramTypes[i + 1]));
		return !hasBindingResult;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
