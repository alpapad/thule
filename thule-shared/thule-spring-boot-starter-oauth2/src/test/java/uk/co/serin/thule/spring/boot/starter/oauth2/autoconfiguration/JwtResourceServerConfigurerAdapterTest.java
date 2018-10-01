package uk.co.serin.thule.spring.boot.starter.oauth2.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JwtResourceServerConfigurerAdapterTest {
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Spy
    private ExpressionUrlAuthorizationConfigurer expressionUrlAuthorizationConfigurer = new ExpressionUrlAuthorizationConfigurer(applicationContext);
    @InjectMocks
    private HttpSecurity httpSecurity;
    @InjectMocks
    private JwtResourceServerConfigurerAdapter sut;
    @Mock
    private ObjectPostProcessor objectPostProcessor;
    @Mock
    private ResourceServerSecurityConfigurer resourceServerSecurityConfigurer;
    @Mock
    private Map<Class<? extends Object>, Object> sharedObjects;
    @Mock
    private DefaultTokenServices tokenServices;

    @Test
    public void configure_configures_token_services() {
        //Given

        //When
        sut.configure(resourceServerSecurityConfigurer);

        //Then
        verify(resourceServerSecurityConfigurer).tokenServices(any());
    }

    @Test
    public void configure_configures_token_servicess() throws Exception {
        //Given

        //When
        sut.configure(httpSecurity);

        //Then
    }
}