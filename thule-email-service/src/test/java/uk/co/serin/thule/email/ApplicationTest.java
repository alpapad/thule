package uk.co.serin.thule.email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
    @Mock
    private SpringApplication springApplication;

    @Test
    public void application_starts_spring_boot() {
        // Given
        String[] args = new String[0];
        ReflectionTestUtils.setField(Application.class, "springApplication", springApplication);

        given(springApplication.run(args)).willReturn(new StaticApplicationContext());

        // When
        Application.main(args);

        // Then (if the test does not throw an exception, it has succeeded)
    }

    @Test
    public void default_constructor_creates_instance_successfully() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        assertThat(BeanUtils.instantiateClass(Application.class)).isNotNull();
    }
}