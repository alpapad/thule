package com.gohenry.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @InjectMocks
    private ApplicationConfigurer applicationConfigurer;
    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() {
        // Given

        // When
        ApplicationConfigurer applicationConfigurer = new ApplicationConfigurer();

        // Then
        assertThat(applicationConfigurer).isNotNull();
    }
}