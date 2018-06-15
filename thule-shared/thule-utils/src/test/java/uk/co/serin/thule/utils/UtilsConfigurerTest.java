package uk.co.serin.thule.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsConfigurerTest {
    @Test
    public void default_constructor_creates_instance_successfully() {
        assertThat(new UtilsConfigurer()).isNotNull();
    }
}