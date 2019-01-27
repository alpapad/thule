package uk.co.serin.thule.utils.service.data.auditor;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuditorConfigurerTest {
    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() {
        // Given

        // When
        AuditorConfigurer auditorConfigurer = new AuditorConfigurer();

        // Then
        assertThat(auditorConfigurer).isNotNull();
    }
}