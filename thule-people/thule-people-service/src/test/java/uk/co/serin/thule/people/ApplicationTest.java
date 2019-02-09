package uk.co.serin.thule.people;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
    @Mock
    private SpringApplication springApplication;

    @Test
    public void when_application_starts_then_spring_boot_runs() {
        // Given
        ReflectionTestUtils.setField(Application.class, "springApplication", springApplication);

        // When
        Application.main(new String[]{});

        // Then
        verify(springApplication).run();
    }

    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() {
        assertThat(new Application()).isNotNull();
    }
}