package com.gohenry.spring.boot.starter.oauth2.autoconfiguration;

import org.junit.Test;

import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class Oauth2PropertiesTest {
    @Test
    public void when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
        assertPojoMethodsFor(Oauth2Properties.class)
                .testing(Method.SETTER, Method.GETTER).areWellImplemented();
    }
}
