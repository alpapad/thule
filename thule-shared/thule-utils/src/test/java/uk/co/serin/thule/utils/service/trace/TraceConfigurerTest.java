package uk.co.serin.thule.utils.service.trace;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TraceConfigurerTest {
    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() {
        // Given

        // When
        TraceConfigurer traceConfigurer = new TraceConfigurer();

        // Then
        assertThat(traceConfigurer).isNotNull();
    }
}