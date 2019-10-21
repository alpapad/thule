package uk.co.serin.thule.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
    @Mock
    private SpringApplication springApplication;

    @Test
    public void when_application_starts_spring_boot_then_spring_boot_runs() {
        // Given
        var args = new String[0];
        ReflectionTestUtils.setField(Application.class, "springApplication", springApplication);

        // When
        Application.main(args);

        // Then
        verify(springApplication).run(args);
    }
}