package uk.co.serin.thule.test;

import org.junit.Test;

import java.lang.instrument.IllegalClassFormatException;

public class ExceptionConstructorTesterTest {
    @Test
    public void all_constructors_exist_with_runtime_exception() {
        // Given
        ExceptionConstructorTester sut = new ExceptionConstructorTester(RuntimeException.class);

        // When
        sut.assertThatAllConstructorsExist();

        // Then
    }

    @Test(expected = IllegalStateException.class)
    public void an_exception_without_all_constructors_throws_an_exception() {
        // Given
        ExceptionConstructorTester sut = new ExceptionConstructorTester(IllegalClassFormatException.class);

        // When
        sut.assertThatAllConstructorsExist();

        // Then
    }
}