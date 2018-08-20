package uk.co.serin.thule.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;

import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
    @Mock
    private SpringApplication springApplication;

    @Test
    public void when_application_starts_spring_boot_then_no_exception_is_thrown() {
        // Given
        String[] args = new String[0];
        ReflectionTestUtils.setField(Application.class, "springApplication", springApplication);

        given(springApplication.run(args)).willReturn(new StaticApplicationContext());

        // When
        uk.co.serin.thule.discovery.Application.main(args);

        // Then (if the test does not throw an exception, it has succeeded)
    }

    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        assertThat(BeanUtils.instantiateClass(Application.class)).isNotNull();
    }
}