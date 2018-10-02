package uk.co.serin.thule.gateway;

import org.junit.Test;

import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ApplicationPropertiesTest {

    @Test
    public void healthcheck_when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
        assertPojoMethodsFor(ApplicationProperties.HealthCheck.class)
                .testing(Method.SETTER, Method.GETTER).areWellImplemented();
    }

    @Test
    public void when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
        assertPojoMethodsFor(ApplicationProperties.class)
                .testing(Method.SETTER, Method.GETTER).areWellImplemented();
    }
}
