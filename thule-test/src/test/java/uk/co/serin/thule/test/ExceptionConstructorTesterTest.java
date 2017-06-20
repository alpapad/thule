package uk.co.serin.thule.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionConstructorTesterTest {
    private ExceptionConstructorTester exceptionConstructorTester = new ExceptionConstructorTester(RuntimeException.class);

    @Test
    public void assert_that_all_constructors_exist_does_not_throw_an_exception_when_all_constructors_exist() {
        // Given

        // When
        exceptionConstructorTester.assertThatAllConstructorsExist();

        // Then
    }

    @Test(expected = IllegalStateException.class)
    public void assert_that_all_constructors_exist_rethrows_exception_as_an_IllegalStateException() {
        // Given
        exceptionConstructorTester = new ExceptionConstructorTester(Class.class);

        // When
        exceptionConstructorTester.assertThatAllConstructorsExist();

        // Then
    }
}