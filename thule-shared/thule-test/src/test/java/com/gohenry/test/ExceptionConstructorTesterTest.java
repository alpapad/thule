package com.gohenry.test;

import org.junit.Test;

import java.lang.instrument.IllegalClassFormatException;

public class ExceptionConstructorTesterTest {
    @Test
    public void all_constructors_exist_with_runtime_exception() {
        // Given
        ExceptionConstructorTester exceptionConstructorTester = new ExceptionConstructorTester(RuntimeException.class);

        // When
        exceptionConstructorTester.assertThatAllConstructorsExist();

        // Then
    }

    @Test(expected = IllegalStateException.class)
    public void an_exception_without_all_constructors_throws_an_exception() {
        // Given
        ExceptionConstructorTester exceptionConstructorTester = new ExceptionConstructorTester(IllegalClassFormatException.class);

        // When
        exceptionConstructorTester.assertThatAllConstructorsExist();

        // Then
    }
}