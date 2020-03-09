package uk.co.serin.thule.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.authentication.keycloak.KeycloakProperties;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @Mock
    private KeycloakProperties keycloakProperties;
    @InjectMocks
    private ApplicationConfigurer sut;

    @Test
    public void when_creating_classLoaderTemplateResolver_then_an_instance_is_created() {
        // When
        var classLoaderTemplateResolver = sut.classLoaderTemplateResolver();

        // Then
        assertThat(classLoaderTemplateResolver).isNotNull();
    }

    @Test
    public void when_creating_keycloakRepository_then_an_instance_is_created() {
        // When
        var keycloakRepository = sut.keycloakRepository(keycloakProperties);

        // Then
        assertThat(keycloakRepository).isNotNull();
    }
}