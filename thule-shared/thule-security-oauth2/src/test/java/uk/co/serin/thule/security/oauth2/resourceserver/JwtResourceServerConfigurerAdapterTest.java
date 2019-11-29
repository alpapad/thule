package uk.co.serin.thule.security.oauth2.resourceserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;

import static org.mockito.ArgumentMatchers.any;
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
    @InjectMocks
    private JwtResourceServerConfigurerAdapter sut;

    @Test
    public void when_configure_http_security_then_security_is_configured() throws Exception {
        // Given
        given(httpSecurity.authorizeRequests()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.requestMatchers(any(EndpointRequest.EndpointRequestMatcher.class))).willReturn(authorizedUrl);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.antMatchers(HttpMethod.OPTIONS, "/**")).willReturn(authorizedUrl);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.antMatchers("/**")).willReturn(authorizedUrl);
        given(authorizedUrl.authenticated()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.and()).willReturn(httpSecurity);
        given(httpSecurity.httpBasic()).willReturn(httpBasicConfigurer);
        given(httpBasicConfigurer.disable()).willReturn(httpSecurity);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);

        // When
        sut.configure(httpSecurity);

        // Then
        verify(expressionInterceptUrlRegistry).antMatchers(HttpMethod.OPTIONS, "/**");
        verify(expressionInterceptUrlRegistry).antMatchers("/**");
    }
}