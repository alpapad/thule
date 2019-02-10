package uk.co.serin.thule.people;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() {
        // When
        ApplicationConfigurer applicationConfigurer = new ApplicationConfigurer();

        // Then
        assertThat(applicationConfigurer).isNotNull();
    }
}