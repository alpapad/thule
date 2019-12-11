package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import uk.co.serin.thule.security.oauth2.context.UserIdEnhancedUserAuthenticationConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ResourceServerAutoConfigurationTest {
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl;
    @Mock
    private CorsConfigurer corsConfigurer;
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry;
    @Mock
    private HttpSecurity httpSecurity;
    @InjectMocks
    private ResourceServerAutoConfiguration sut;
    @Mock
    private UserIdEnhancedUserAuthenticationConverter userIdEnhancedUserAuthenticationConverter;

    @Test
    public void when_jwtResourceServerConfigurerAdapter_then_an_instance_is_instantiated() throws Exception {
        // Given
        given(httpSecurity.cors()).willReturn(corsConfigurer);
        given(corsConfigurer.and()).willReturn(httpSecurity);
        given(httpSecurity.authorizeRequests()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.requestMatchers(any(EndpointRequest.EndpointRequestMatcher.class))).willReturn(authorizedUrl);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.antMatchers("/v2/api-docs")).willReturn(authorizedUrl);
        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.antMatchers("/**")).willReturn(authorizedUrl);
        given(authorizedUrl.authenticated()).willReturn(expressionInterceptUrlRegistry);

        // When
        var resourceServerConfigurerAdapter = sut.resourceServerConfigurerAdapter();
        resourceServerConfigurerAdapter.configure(httpSecurity);

        // Then
        assertThat(resourceServerConfigurerAdapter).isNotNull();
    }

    @Test
    public void when_jwtAccessTokenCustomizer_then_an_instance_is_instantiated() {
        // When
        var accessTokenConverter = sut.jwtAccessTokenCustomizer(userIdEnhancedUserAuthenticationConverter);

        // Then
        assertThat(accessTokenConverter).isNotNull();
    }
}