package uk.co.serin.thule.spring.boot.starter.autoconfiguration;

import org.junit.Test;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

public class ThuleAuditorAutoConfigurationTest {

    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() {
        // Given

        // When
        ThuleAuditorAutoConfiguration thuleAuditorAutoConfiguration = new ThuleAuditorAutoConfiguration();

        // Then
        assertThat(thuleAuditorAutoConfiguration).isNotNull();
    }
}