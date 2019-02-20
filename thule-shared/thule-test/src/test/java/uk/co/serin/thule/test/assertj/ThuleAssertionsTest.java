package uk.co.serin.thule.test.assertj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.HttpStatusCodeException;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ThuleAssertionsTest {
    @Mock
    private HttpStatusCodeException httpStatusCodeException;

    @Test
    public void when_asserting_for_actuator_uri_then_assert_instance_is_created() {
        // When
        var actualSpringBootActuatorAssert = assertThat(new ActuatorUri(URI.create("http://localhost")));

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

    @Test
    public void when_asserting_for_http_status_code_exception_then_assert_instance_is_created() {
        //When
        var httpStatusCodeExceptionAssert = assertThat(httpStatusCodeException);

        //Then
        assertThat(httpStatusCodeExceptionAssert).isNotNull();
    }
}