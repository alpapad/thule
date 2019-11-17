package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JwtResourceServerConfigurerAdapterTest {
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl;
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry;
    @Mock
    private HttpBasicConfigurer<HttpSecurity> httpBasicConfigurer;
    @Mock
    private HttpSecurity httpSecurity;
    @Mock
    private ResourceServerSecurityConfigurer resourceServerSecurityConfigurer;
    @InjectMocks
    private JwtResourceServerConfigurerAdapter sut;

    @Test
    public void configure_configures_token_services() {
        // When
        sut.configure(resourceServerSecurityConfigurer);

        // Then
        verify(resourceServerSecurityConfigurer).tokenServices(any());
    }

    @Test
    public void configure_configures_token_servicess() throws Exception {
        // Given
        given(httpSecurity.authorizeRequests()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.requestMatchers(any())).willReturn(authorizedUrl);
        given(expressionInterceptUrlRegistry.antMatchers(anyString())).willReturn(authorizedUrl);
        given(authorizedUrl.authenticated()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.and()).willReturn(httpSecurity);
        given(httpSecurity.httpBasic()).willReturn(httpBasicConfigurer);
        given(httpBasicConfigurer.disable()).willReturn(httpSecurity);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);

        // When
        sut.configure(httpSecurity);

        // Then
        verify(expressionInterceptUrlRegistry).antMatchers("/apidocs/**");
        verify(expressionInterceptUrlRegistry).antMatchers("/**");
    }
}