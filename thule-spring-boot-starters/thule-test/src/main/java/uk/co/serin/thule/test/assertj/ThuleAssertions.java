package uk.co.serin.thule.test.assertj;

import org.assertj.core.api.Assertions;

public class ThuleAssertions extends Assertions {
    public static SpringBootActuatorAssert assertThat(ActuatorUri actual) {
        return new SpringBootActuatorAssert(actual);
    }
}