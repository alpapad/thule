package uk.co.serin.thule.test.assertj;

import org.junit.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class ActuatorUriTest {
    @Test

    public void when_creating_via_string_static_factory_method_then_actuator_uri_contains_uri() {
        // Given
        var uri = "http://localhost";

        // When
        var actuatorUri = ActuatorUri.of(uri);

        // Then
        assertThat(actuatorUri).isNotNull();
        assertThat(actuatorUri.getUri().toString()).isEqualTo(uri);
    }

    @Test
    public void when_creating_via_uri_static_factory_method_then_actuator_uri_contains_uri() {
        // Given
        var uri = URI.create("http://localhost");

        // When
        var actuatorUri = ActuatorUri.of(uri);

        // Then
        assertThat(actuatorUri).isNotNull();
        assertThat(actuatorUri.getUri()).isEqualTo(uri);
    }
}