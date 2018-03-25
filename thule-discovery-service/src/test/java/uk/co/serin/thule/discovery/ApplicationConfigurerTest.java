package uk.co.serin.thule.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @InjectMocks
    private ApplicationConfigurer applicationConfigurer;

    @Test
    public void applicationConfigurer_instantiates_without_an_exception() {
        // Given

        // When
        ApplicationConfigurer applicationConfigurer = new ApplicationConfigurer();

        // Then
        assertThat(applicationConfigurer).isNotNull();
    }
}