package com.gohenry.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsConfigurerTest {
    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() {
        assertThat(new UtilsConfigurer()).isNotNull();
    }
}