package uk.co.serin.thule.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @InjectMocks
    private uk.co.serin.thule.discovery.ApplicationConfigurer applicationConfigurer;

    @Test
    public void applicationConfigurer_instantiates_without_an_exception() {
        // Given

        // When
        uk.co.serin.thule.discovery.ApplicationConfigurer applicationConfigurer = new uk.co.serin.thule.discovery.ApplicationConfigurer();

        // Then
        assertThat(applicationConfigurer).isNotNull();
    }
}