package uk.co.serin.thule.people;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.rest.core.config.RepositoryCorsRegistry;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CorsRepositoryRestConfigurerTest {
    @Mock
    private CorsRegistration corsRegistration;
    @InjectMocks
    private CorsRepositoryRestConfigurer corsRepositoryRestConfigurer;
    @Mock
    private RepositoryCorsRegistry repositoryCorsRegistry;
    @Mock
    private RepositoryRestConfiguration repositoryRestConfiguration;

    @Test
    public void configurer_allows_all_paths_and_all_methods() {
        // Given
        given(repositoryRestConfiguration.getCorsRegistry()).willReturn(repositoryCorsRegistry);
        given(repositoryCorsRegistry.addMapping("/**")).willReturn(corsRegistration);

        // When
        corsRepositoryRestConfigurer.configureRepositoryRestConfiguration(repositoryRestConfiguration);

        // Then
        verify(corsRegistration).allowedMethods("*");
    }
}