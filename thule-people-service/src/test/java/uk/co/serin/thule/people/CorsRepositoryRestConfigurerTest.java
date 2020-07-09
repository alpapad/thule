package uk.co.serin.thule.people;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.core.config.RepositoryCorsRegistry;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CorsRepositoryRestConfigurerTest {
    @Mock
    private CorsRegistration corsRegistration;
    @Mock
    private RepositoryCorsRegistry repositoryCorsRegistry;
    @Mock
    private RepositoryRestConfiguration repositoryRestConfiguration;
    @InjectMocks
    private CorsRepositoryRestConfigurer sut;

    @Test
    void when_configure_repository_rest_configuration_then_cors_is_configured_for_all_mappings_and_methods() {
        // Given
        given(repositoryRestConfiguration.getCorsRegistry()).willReturn(repositoryCorsRegistry);
        given(repositoryCorsRegistry.addMapping("/**")).willReturn(corsRegistration);
        given(corsRegistration.allowedMethods("*")).willReturn(corsRegistration);
        given(corsRegistration.allowedOrigins("*")).willReturn(corsRegistration);

        // When
        sut.configureRepositoryRestConfiguration(repositoryRestConfiguration);

        // Then
        verify(repositoryCorsRegistry).addMapping("/**");
        verify(corsRegistration).allowedMethods("*");
        verify(corsRegistration).allowedOrigins("*");
    }
}