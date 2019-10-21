package uk.co.serin.thule.gateway.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MicroServiceDelegatingJwtResourceServerConfigurerAdapterTest {
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl;
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry;
    @Mock
    private HttpBasicConfigurer<HttpSecurity> httpBasicConfigurer;
    @Mock
    private HttpSecurity httpSecurity;
    @InjectMocks
    private MicroServiceDelegatingJwtResourceServerConfigurerAdapter sut;

    @Test
    public void given_authentication_when_configuring_http_security_then_access_to_all_urls_is_allowed() throws Exception {
        // Given
        given(httpSecurity.authorizeRequests()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.requestMatchers(any())).willReturn(authorizedUrl);
        given(expressionInterceptUrlRegistry.antMatchers(any(), any())).willReturn(authorizedUrl);
        given(expressionInterceptUrlRegistry.antMatchers(anyString())).willReturn(authorizedUrl);
        given(authorizedUrl.authenticated()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.and()).willReturn(httpSecurity);
        given(httpSecurity.httpBasic()).willReturn(httpBasicConfigurer);
        given(httpBasicConfigurer.disable()).willReturn(httpSecurity);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);

        // When
        sut.configure(httpSecurity);

        // Then
        verify(expressionInterceptUrlRegistry).antMatchers("/**");
    }

    @Test
    public void given_no_authentication_when_configuring_http_security_then_access_to_gohenry_services_is_allowed() throws Exception {
        // Given
        given(httpSecurity.authorizeRequests()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.requestMatchers(any())).willReturn(authorizedUrl);
        given(expressionInterceptUrlRegistry.antMatchers(any(), any())).willReturn(authorizedUrl);
        given(expressionInterceptUrlRegistry.antMatchers(anyString())).willReturn(authorizedUrl);
        given(authorizedUrl.authenticated()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.and()).willReturn(httpSecurity);
        given(httpSecurity.httpBasic()).willReturn(httpBasicConfigurer);
        given(httpBasicConfigurer.disable()).willReturn(httpSecurity);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);

        // When
        sut.configure(httpSecurity);

        // Then
        verify(expressionInterceptUrlRegistry).antMatchers("/thule-*-service/**");
    }

    @Test
    public void given_no_authentication_when_configuring_http_security_then_access_to_the_actuator_is_allowed() throws Exception {
        // Given
        given(httpSecurity.authorizeRequests()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.requestMatchers(any())).willReturn(authorizedUrl);
        given(expressionInterceptUrlRegistry.antMatchers(any(), any())).willReturn(authorizedUrl);
        given(expressionInterceptUrlRegistry.antMatchers(anyString())).willReturn(authorizedUrl);
        given(authorizedUrl.authenticated()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.and()).willReturn(httpSecurity);
        given(httpSecurity.httpBasic()).willReturn(httpBasicConfigurer);
        given(httpBasicConfigurer.disable()).willReturn(httpSecurity);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);

        // When
        sut.configure(httpSecurity);

        // Then
        verify(expressionInterceptUrlRegistry).antMatchers("/*/actuator/**");
    }

    @Test
    public void given_no_authentication_when_configuring_http_security_then_options_http_method_is_allowed_for_all_urls() throws Exception {
        // Given
        given(httpSecurity.authorizeRequests()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.requestMatchers(any())).willReturn(authorizedUrl);
        given(expressionInterceptUrlRegistry.antMatchers(any(), any())).willReturn(authorizedUrl);
        given(expressionInterceptUrlRegistry.antMatchers(anyString())).willReturn(authorizedUrl);
        given(authorizedUrl.authenticated()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.and()).willReturn(httpSecurity);
        given(httpSecurity.httpBasic()).willReturn(httpBasicConfigurer);
        given(httpBasicConfigurer.disable()).willReturn(httpSecurity);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);

        // When
        sut.configure(httpSecurity);

        // Then
        verify(expressionInterceptUrlRegistry).antMatchers(HttpMethod.OPTIONS, "/**");
    }
}