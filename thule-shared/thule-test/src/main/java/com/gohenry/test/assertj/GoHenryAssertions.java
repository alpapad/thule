package com.gohenry.test.assertj;

import org.assertj.core.api.Assertions;

public class GoHenryAssertions extends Assertions {
    public static SpringBootActuatorAssert assertThat(ActuatorUri actual) {
        return new SpringBootActuatorAssert(actual);
    }
}