package uk.co.serin.thule.gateway;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationPropertiesTest {
    private ApplicationProperties sut = new ApplicationProperties();

    @Test
    public void when_healthcheck_inner_class_is_initialized_then_healthcheck_is_not_null() {
        // When
        var healthCheck = sut.getHealthCheck();

        // Then
        assertThat(healthCheck).isNotNull();
    }
}
