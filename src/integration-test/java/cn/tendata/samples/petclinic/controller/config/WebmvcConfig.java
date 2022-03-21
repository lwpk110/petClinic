package cn.tendata.samples.petclinic.controller.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL;

import cn.tendata.samples.petclinic.bind.support.RequestBodyPathVariableMethodArgumentResolver;
import cn.tendata.samples.petclinic.data.jpa.jackson.databind.PageSerializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(basePackages = "cn.tendata.samples.petclinic.controller")
public class WebmvcConfig implements WebMvcConfigurer {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public Jackson2ObjectMapperBuilder configureObjectMapper() {
		return new Jackson2ObjectMapperBuilder()
			.modulesToInstall(jacksonHibernate5Module(), jacksonPageWithJsonViewModule());
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		registrar.setUseIsoFormat(true);
		registrar.registerFormatters(registry);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().modules(
				new JavaTimeModule(),    			// java date time 序列化支持
				jacksonHibernate5Module(),    		//hibernate 数据序列化支持
				jacksonPageWithJsonViewModule())    // page with json view 解析支持
			.deserializers()
			.featuresToEnable(READ_UNKNOWN_ENUM_VALUES_AS_NULL) // 为空或者不识别的enum 视为null
			.serializers().featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.dateFormat(new StdDateFormat()) 		//时间反序列化 iso ，local以本地服务器为主
			.build();

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(
			objectMapper);
		converter.setDefaultCharset(StandardCharsets.UTF_8);
		converters.add(converter);
	}

	@Bean
	public Module jacksonHibernate5Module() {
		Hibernate5Module module = new Hibernate5Module();
		return module;
	}

	@Bean
	public Module jacksonPageWithJsonViewModule() {
		SimpleModule module = new SimpleModule("jackson-page-with-jsonview",
			Version.unknownVersion());
		module.addSerializer(PageImpl.class, new PageSerializer());
		return module;
	}

	@Bean
	public RequestBodyPathVariableMethodArgumentResolver requestBodyPathVariableMethodArgumentResolver() {
		return new RequestBodyPathVariableMethodArgumentResolver();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(requestBodyPathVariableMethodArgumentResolver());
	}
}
