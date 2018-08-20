package uk.co.serin.thule.email.service;

import com.gohenry.test.ExceptionConstructorTester;

import org.junit.Test;

public class ExceptionsTest {
    @Test
    public void when_exceptions_then_all_constructors_should_exist() {
        // Given

        // When
        new ExceptionConstructorTester(EmailServiceException.class).assertThatAllConstructorsExist();

        // Then (throws exception if constructors do not exist)
    }
}