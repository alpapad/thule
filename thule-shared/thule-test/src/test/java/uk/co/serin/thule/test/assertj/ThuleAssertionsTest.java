package uk.co.serin.thule.test.assertj;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import pl.pojo.tester.api.assertion.Method;

import java.net.URI;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ThuleAssertionsTest {

    @Test
    public void constructor_is_well_implemented() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(ThuleAssertions.class).testing(Method.CONSTRUCTOR).areWellImplemented();
    }

    @Test
    public void entrypoint_for_SpringBootActuatorAssert_exists() {
        // Given

        // When
        SpringBootActuatorAssert actualSpringBootActuatorAssert = assertThat(new ActuatorUri(URI.create("http://localhost")));

        // Then
        Assertions.assertThat(actualSpringBootActuatorAssert).isNotNull();
    }

}