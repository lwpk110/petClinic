package cn.tendata.samples.petclinic.config;

import cn.tendata.samples.petclinic.bind.support.RequestBodyPathVariableMethodArgumentResolver;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestWebMvcConfig implements WebMvcConfigurer {
/*	private static final String dateFormat = "yyyy-MM-dd";
	private static final String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> {
			builder.simpleDateFormat(dateTimeFormat);
			builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
			LocalDateTimeSerializer utc = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat).withZone(ZoneId.of("UTC")));
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("");
			builder.serializers(utc);
		};
	}*/
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
		registrar.setDateTimeFormatter(DateTimeFormatter.ISO_INSTANT);
        registrar.registerFormatters(registry);
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
