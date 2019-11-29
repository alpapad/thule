package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import uk.co.serin.thule.security.oauth2.Oauth2Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class Oauth2AutoConfigurationTest {
    @Mock
    private CorsRegistration corsRegistration;
    @Mock
    private CorsRegistry corsRegistry;
    @Mock
    private Oauth2Properties oauth2Properties;
    @InjectMocks
    private Oauth2AutoConfiguration sut;

    @Test
    public void when_access_token_customizer_then_an_instance_is_instantiated() {
        // When
        var accessTokenConverter = sut.jwtAccessTokenCustomizer();

        // Then
        assertThat(accessTokenConverter).isNotNull();
    }

    @Test
    public void when_cors_configurer_then_an_instance_is_instantiated() {
        // Given
        given(corsRegistry.addMapping("/**")).willReturn(corsRegistration);
        given(corsRegistration.allowedMethods(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(), HttpMethod.POST.name()))
                .willReturn(corsRegistration);

        //When
        var corsConfigurer = sut.corsConfigurer();
        corsConfigurer.addCorsMappings(corsRegistry);

        // Then
        assertThat(corsConfigurer).isNotNull();
    }
}