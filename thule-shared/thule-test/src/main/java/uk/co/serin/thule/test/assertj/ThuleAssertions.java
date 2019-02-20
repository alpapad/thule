package uk.co.serin.thule.test.assertj;

import org.assertj.core.api.Assertions;
import org.springframework.web.client.HttpStatusCodeException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ThuleAssertions extends Assertions {
    public static SpringBootActuatorAssert assertThat(ActuatorUri actual) {
        return new SpringBootActuatorAssert(actual);
    }

    public static HttpStatusCodeExceptionAssert assertThat(HttpStatusCodeException actual) {
        return new HttpStatusCodeExceptionAssert(actual);
    }
}