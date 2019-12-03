package uk.co.serin.thule.spring.boot.starter.autoconfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class WebMvcAutoConfigurationTest {
    @Mock
    private CorsRegistration corsRegistration;
    @Mock
    private CorsRegistry corsRegistry;
    @InjectMocks
    private WebMvcAutoConfiguration sut;

    @Test
    public void when_corsConfigurer_then_cors_mappings_is_configured() {
        // Given
        given(corsRegistry.addMapping("/**")).willReturn(corsRegistration);

        //When
        var corsConfigurer = sut.corsConfigurer();
        corsConfigurer.addCorsMappings(corsRegistry);

        //Then
        assertThat(corsConfigurer).isNotNull();
    }
}