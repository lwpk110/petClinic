package cn.tendata.samples.petclinic.controller.config;


import cn.tendata.samples.petclinic.service.EntityService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

@Profile("test")
@Configuration
@ComponentScan(basePackageClasses = {EntityService.class})
@Import({JpaConfig.class, WebmvcConfig.class})
@TestPropertySource("classpath:/application.properties")
@EnableConfigurationProperties
public class TestConfig {

}
