package uk.co.serin.thule.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @InjectMocks
    private uk.co.serin.thule.discovery.ApplicationConfigurer applicationConfigurer;

    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() {
        // Given

        // When
        uk.co.serin.thule.discovery.ApplicationConfigurer applicationConfigurer = new uk.co.serin.thule.discovery.ApplicationConfigurer();

        // Then
        assertThat(applicationConfigurer).isNotNull();
    }
}