package uk.co.serin.thule.test;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionConstructorTester {
    private final Class<?> clazz;

    public ExceptionConstructorTester(Class<? extends Throwable> clazz) {
        this.clazz = clazz;
    }

    public void assertThatAllConstructorsExist() {
        try {
            assertThatConstructorWithMessageExists();
            assertThatConstructorWithMessageAndThrowableExists();
            assertThatConstructorWithMessageExecutes();
            assertThatConstructorWithMessageAndThrowableExecutes();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void assertThatConstructorWithMessageExists() throws NoSuchMethodException {
        // Given

        // When
        clazz.getConstructor(String.class);

        // Then (throws exception when the test fails)
    }

    private void assertThatConstructorWithMessageAndThrowableExists() throws NoSuchMethodException {
        // Given

        // When
        clazz.getConstructor(String.class, Throwable.class);

        // Then (throws exception when the test fails)
    }

    private void assertThatConstructorWithMessageExecutes()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        // Given
        String message = "Test message";

        // When
        Throwable t = (Throwable) clazz.getConstructor(String.class).newInstance(message);

        // Then
        assertThat(t.getMessage()).contains(message);
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    private void assertThatConstructorWithMessageAndThrowableExecutes()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        // Given
        String message = "Test message";
        RuntimeException cause = new TestException();

        // When
        Throwable t = (Throwable) clazz.getConstructor(String.class, Throwable.class).newInstance(message, cause);

        // Then
        assertThat(t.getMessage()).contains(message);
    }

    private static class TestException extends RuntimeException {
        private static final long serialVersionUID = -4398980093932344157L;
    }
}
