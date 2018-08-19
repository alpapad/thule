package uk.co.serin.thule.people.domain.address;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class AddressTest {
    @Test
    public void when_equals_is_overridden_then_verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Address.class).
                withPrefabValues(Action.class, new Action(ActionCode.ADDRESS_DISABLE), new Action(ActionCode.ADDRESS_DISCARD)).
                withOnlyTheseFields(Address.ENTITY_ATTRIBUTE_NAME_ADDRESS_LINE_1, Address.ENTITY_ATTRIBUTE_NAME_COUNTRY, Address.ENTITY_ATTRIBUTE_NAME_POST_CODE).
                verify();
    }

    @Test
    public void when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
        assertPojoMethodsFor(Address.class, FieldPredicate.exclude("addressLine1", "country", "postCode")).
                testing(Method.SETTER).areWellImplemented();
        assertPojoMethodsFor(Address.class).
                testing(Method.CONSTRUCTOR, Method.GETTER, Method.TO_STRING).areWellImplemented();
    }
}