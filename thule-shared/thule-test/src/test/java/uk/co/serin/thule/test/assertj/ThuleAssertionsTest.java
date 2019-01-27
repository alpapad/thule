package uk.co.serin.thule.test.assertj;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.HttpStatusCodeException;

import pl.pojo.tester.api.assertion.Method;

import java.net.URI;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ThuleAssertionsTest {

    @Test
    public void constructor_is_well_implemented() {
        // Then
        assertPojoMethodsFor(ThuleAssertions.class).testing(Method.CONSTRUCTOR).areWellImplemented();
    }

    @Test
    public void entrypoint_for_SpringBootActuatorAssert_exists() {
        // When
        SpringBootActuatorAssert actualSpringBootActuatorAssert = assertThat(new ActuatorUri(URI.create("http://localhost")));

        // Then
        Assertions.assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

    @Test
    public void when_asserting_for_http_status_code_exception_assert_then_assert_instance_is_created() {

        //Given
        var httpStatusCodeException = Mockito.mock(HttpStatusCodeException.class);

        //When
        var httpStatusCodeExceptionAssert = assertThat(httpStatusCodeException);

        //Then
        Assertions.assertThat(httpStatusCodeExceptionAssert).isNotNull();
    }
}