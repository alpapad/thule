package uk.co.serin.thule.spring.boot.starter.resourceserver.autoconfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.resourceserver.decoder.KeycloakUnsignedJwtDecoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ResourceServerAutoConfigurationTest {
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl;
    @Mock
    private CorsConfigurer<HttpSecurity> corsConfigurer;
    @Mock
    private Environment environment;
    @Mock
    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry;
    @Mock
    private HttpSecurity httpSecurity;
    @Mock
    private OAuth2ResourceServerProperties.Jwt jwt;
    @Mock
    private OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer jwtConfigurer;
    @Mock
    private NimbusJwtDecoder nimbusJwtDecoder;
    @Mock
    private OAuth2ResourceServerConfigurer<HttpSecurity> oAuth2ResourceServerConfigurer;
    @Mock
    private OAuth2ResourceServerProperties oAuth2ResourceServerProperties;
    @Spy
    private ResourceServerAutoConfiguration sut;

    @Test
    public void when_configure_http_security_then_it_is_configured() throws Exception {
        // Given
        given(httpSecurity.cors()).willReturn(corsConfigurer);
        given(corsConfigurer.and()).willReturn(httpSecurity);

        given(httpSecurity.authorizeRequests()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.antMatchers("/actuator/**")).willReturn(authorizedUrl);

        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.antMatchers("/v2/api-docs")).willReturn(authorizedUrl);

        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.antMatchers("/swagger-ui.html")).willReturn(authorizedUrl);

        given(authorizedUrl.permitAll()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.antMatchers("/**")).willReturn(authorizedUrl);

        given(authorizedUrl.authenticated()).willReturn(expressionInterceptUrlRegistry);
        given(expressionInterceptUrlRegistry.and()).willReturn(httpSecurity);

        given(httpSecurity.oauth2ResourceServer()).willReturn(oAuth2ResourceServerConfigurer);
        given(oAuth2ResourceServerConfigurer.jwt()).willReturn(jwtConfigurer);

        given(jwtConfigurer.jwtAuthenticationConverter(any())).willReturn(jwtConfigurer);

        ReflectionTestUtils.setField(sut, "context", applicationContext);
        given(applicationContext.getEnvironment()).willReturn(environment);

        // When
        sut.configure(httpSecurity);

        // Then
        verify(expressionInterceptUrlRegistry, times(3)).antMatchers(anyString());
    }

    @Test
    public void when_jwtAuthenticationConverter_then_an_instance_is_instantiated() {
        // Given
        ReflectionTestUtils.setField(sut, "context", applicationContext);
        given(applicationContext.getEnvironment()).willReturn(environment);

        // When
        var jwtAuthenticationConverter = sut.jwtAuthenticationConverter();

        // Then
        assertThat(jwtAuthenticationConverter).isNotNull();
    }

    @Test
    public void when_jwtDecoderFromIssuerLocation_then_an_instance_is_instantiated() {
        // Given
        given(oAuth2ResourceServerProperties.getJwt()).willReturn(jwt);
        given(jwt.getIssuerUri()).willReturn("http://localhost");
        doReturn(nimbusJwtDecoder).when(sut).getJwtDecoderfromIssuerLocation(any());

        // When
        var jwtDecoder = sut.jwtDecoderFromIssuerLocation(oAuth2ResourceServerProperties);

        // Then
        assertThat(jwtDecoder).isNotNull();
    }

    @Test
    public void when_jwtDecoder_then_an_instance_is_instantiated() {
        // When
        var jwtDecoder = sut.jwtDecoder();

        // Then
        assertThat(jwtDecoder).isNotNull();
        assertThat(jwtDecoder).isInstanceOf(KeycloakUnsignedJwtDecoder.class);
    }

    @Test
    public void when_keycloakResourceRoleConverter_then_an_instance_is_instantiated() {
        // Given
        ReflectionTestUtils.setField(sut, "context", applicationContext);
        given(applicationContext.getEnvironment()).willReturn(environment);

        // When
        var keycloakResourceRoleConverter = sut.keycloakResourceRoleConverter();

        // Then
        assertThat(keycloakResourceRoleConverter).isNotNull();
    }
}