package uk.co.serin.thule.test.assertj;

import org.junit.Test;

import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ActuatorUriTest {

    @Test
    public void when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(ActuatorUri.class).
                testing(Method.CONSTRUCTOR, Method.GETTER).areWellImplemented();
    }
}