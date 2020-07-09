package uk.co.serin.thule.test.assertj;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpStatusCodeException;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ThuleAssertionsTest {
    @Mock
    private HttpStatusCodeException httpStatusCodeException;

    @Test
    void when_asserting_for_actuator_uri_then_assert_instance_is_created() {
        // When
        var actualSpringBootActuatorAssert = assertThat(ActuatorUri.using("http://localhost"));

        // Then
        assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

    @Test
    void when_asserting_for_http_status_code_exception_then_assert_instance_is_created() {
        // When
        var httpStatusCodeExceptionAssert = assertThat(httpStatusCodeException);

        // Then
        assertThat(httpStatusCodeExceptionAssert).isNotNull();
    }
}