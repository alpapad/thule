package uk.co.serin.thule.authentication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.serin.thule.authentication.keycloak.KeycloakProperties;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigurerTest {
    @Mock
    private KeycloakProperties keycloakProperties;
    @InjectMocks
    private ApplicationConfigurer sut;

    @Test
    void when_creating_classLoaderTemplateResolver_then_an_instance_is_created() {
        // When
        var classLoaderTemplateResolver = sut.classLoaderTemplateResolver();

        // Then
        assertThat(classLoaderTemplateResolver).isNotNull();
    }

    @Test
    void when_creating_keycloakRepository_then_an_instance_is_created() {
        // When
        var keycloakRepository = sut.keycloakRepository(keycloakProperties);

        // Then
        assertThat(keycloakRepository).isNotNull();
    }
}