package cn.tendata.samples.petclinic.controller;

import cn.tendata.samples.petclinic.controller.config.TestConfig;
import com.github.database.rider.junit5.DBUnitExtension;
import javax.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({SpringExtension.class, DBUnitExtension.class, RestDocumentationExtension.class})
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@ActiveProfiles("test")
@EnableTransactionManagement
public abstract class MockMvcTestSupport {

    @Resource
    protected WebApplicationContext context;

	protected MockMvc mockMvc;

	@BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
			.build();
    }
}
