package uk.co.serin.thule.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CoreConfigurationTest {
    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        assertThat(new CoreConfiguration()).isNotNull();
    }
}