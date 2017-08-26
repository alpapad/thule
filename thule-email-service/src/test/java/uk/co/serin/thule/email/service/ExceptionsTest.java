package uk.co.serin.thule.email.service;

import org.junit.Test;

import uk.co.serin.thule.test.ExceptionConstructorTester;

public class ExceptionsTest {
    @Test
    public void assert_that_all_exception_constructors_exist() {
        // Given

        // When
        new ExceptionConstructorTester(EmailServiceException.class).assertThatAllConstructorsExist();

        // Then (throws exception if constructors do not exist)
    }
}