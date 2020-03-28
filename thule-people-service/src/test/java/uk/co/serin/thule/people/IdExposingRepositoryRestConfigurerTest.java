package uk.co.serin.thule.people;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IdExposingRepositoryRestConfigurerTest {
    private static final int NO_OF_DOMAIN_MODEL_CLASSES = 8;
    @InjectMocks
    private IdExposingRepositoryRestConfigurer idExposingRepositoryRestConfigurer;
    @Mock
    private RepositoryRestConfiguration repositoryRestConfiguration;

    @Test
    public void when_configure_repository_rest_configuration_then_ids_are_exposed_for_all_domain_models() {
        // When
        idExposingRepositoryRestConfigurer.configureRepositoryRestConfiguration(repositoryRestConfiguration);

        // Then
        var argumentCaptor = ArgumentCaptor.forClass(Class[].class);
        verify(repositoryRestConfiguration).exposeIdsFor(argumentCaptor.capture());
        assertThat(argumentCaptor.getAllValues()).hasSize(NO_OF_DOMAIN_MODEL_CLASSES);
    }
}