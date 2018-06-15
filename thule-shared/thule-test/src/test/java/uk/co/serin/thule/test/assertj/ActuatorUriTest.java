package uk.co.serin.thule.test.assertj;

import org.junit.Test;

import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ActuatorUriTest {

    @Test
    public void pojo_methods_are_well_implemented() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(ActuatorUri.class).
                testing(Method.CONSTRUCTOR, Method.GETTER).areWellImplemented();
    }
}