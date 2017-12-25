package uk.co.serin.thule.people;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurationTest {
    private ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
    @Mock
    private ApplicationContext applicationContext;
    private ExpressionUrlAuthorizationConfigurer expressionUrlAuthorizationConfigurer;
    @Mock
    private HttpSecurity httpSecurity;

    @Test
    public void configure_adds_request_mappings() throws Exception {
        // Given
        ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry registry = expressionUrlAuthorizationConfigurer.getRegistry();
        given(httpSecurity.authorizeRequests()).willReturn(registry);

        // When
        applicationConfiguration.configure(httpSecurity);

        // Then
        List urlMappings = List.class.cast(ReflectionTestUtils.getField(registry, "urlMappings"));
        assertThat(urlMappings).hasSize(2);
    }

    @Before
    public void setUp() {
        expressionUrlAuthorizationConfigurer = new ExpressionUrlAuthorizationConfigurer(applicationContext);
        expressionUrlAuthorizationConfigurer.setBuilder(httpSecurity);
    }
}