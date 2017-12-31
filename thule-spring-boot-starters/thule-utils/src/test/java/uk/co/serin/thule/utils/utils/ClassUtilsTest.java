package uk.co.serin.thule.utils.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassUtilsTest {
    @Test(expected = IllegalStateException.class)
    public void forName_rethrows_classnotfoundexception_as_illegalstateexception() {
        ClassUtils.forName("class_does_not_exist");
    }

    @Test
    public void forName_returns_class_instance() {
        // Given

        // When
        Class<?> clazz = ClassUtils.forName(ClassUtils.class.getName());

        // Then
        assertThat(clazz).isEqualTo(ClassUtils.class);
    }
}