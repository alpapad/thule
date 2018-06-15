package uk.co.serin.thule.people;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IdExposingRepositoryRestConfigurerTest {
    private static final int NO_OF_DOMAIN_MODEL_CLASSES = 8;
    @InjectMocks
    private IdExposingRepositoryRestConfigurer idExposingRepositoryRestConfigurer;
    @Mock
    private RepositoryRestConfiguration repositoryRestConfiguration;

    @Test
    public void configurer_exposes_ids_for_all_domain_models() {
        // Given

        // When
        idExposingRepositoryRestConfigurer.configureRepositoryRestConfiguration(repositoryRestConfiguration);

        // Then
        ArgumentCaptor<Class[]> argumentCaptor = ArgumentCaptor.forClass(Class[].class);
        verify(repositoryRestConfiguration).exposeIdsFor(argumentCaptor.capture());
        assertThat(argumentCaptor.getAllValues()).hasSize(NO_OF_DOMAIN_MODEL_CLASSES);
    }
}