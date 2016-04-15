package uk.co.serin.thule.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.support.StaticApplicationContext;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SpringApplication.class)
public class ApplicationTest {
    @Test
    public void applicationStartsSpringBoot() {
        // Given
        String[] args = new String[0];

        mockStatic(SpringApplication.class);
        given(SpringApplication.run(Application.class, args)).willReturn(new StaticApplicationContext());

        // When
        Application.main(args);

        // Then (if the test does not throw an exception, it has succeeded)
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        assertThat(BeanUtils.instantiateClass(Application.class)).isNotNull();
    }
}