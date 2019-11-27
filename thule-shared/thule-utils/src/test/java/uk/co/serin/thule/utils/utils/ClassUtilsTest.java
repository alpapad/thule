package uk.co.serin.thule.utils.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ClassUtilsTest {
    @Test
    public void given_class_that_does_not_exist_when_forName_then_an_illegal_state_exception_is_thrown() {
        // When
        var throwable = catchThrowable(() -> ClassUtils.forName("class_does_not_exist"));

        // Then
        assertThat(throwable).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void when_forName_then_class_instance_is_returned() {
        // When
        var clazz = ClassUtils.forName(ClassUtils.class.getName());

        // Then
        assertThat(clazz).isEqualTo(ClassUtils.class);
    }
}