package uk.co.serin.thule.email.service;

import org.junit.Test;

import uk.co.serin.thule.test.ExceptionConstructorTester;

public class ExceptionsTest {
    @Test
    public void when_exceptions_then_all_constructors_should_exist() {
        // Given

        // When
        new ExceptionConstructorTester(EmailServiceException.class).assertThatAllConstructorsExist();

        // Then (throws exception if constructors do not exist)
    }
}