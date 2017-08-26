package uk.co.serin.thule.people.domain.state;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import uk.co.serin.thule.people.datafactories.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ActionTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builder_and_getters_operate_on_the_same_field() {
        // Given
        Action expectedAction = testDataFactory.getActions().get(ActionCode.PERSON_ENABLE);

        // When
        Action actualAction = Action.ActionBuilder.anAction().
                withCode(expectedAction.getCode()).
                withCreatedAt(expectedAction.getCreatedAt()).
                withDescription(expectedAction.getDescription()).
                withId(expectedAction.getId()).
                withNextState(expectedAction.getNextState()).
                withUpdatedAt(expectedAction.getUpdatedAt()).
                withUpdatedBy(expectedAction.getUpdatedBy()).
                withVersion(expectedAction.getVersion()).
                build();

        // Then
        assertThat(actualAction.getCode()).isEqualTo(expectedAction.getCode());
        assertThat(actualAction.getCreatedAt()).isEqualTo(expectedAction.getCreatedAt());
        assertThat(actualAction.getId()).isEqualTo(expectedAction.getId());
        assertThat(actualAction.getDescription()).isEqualTo(expectedAction.getDescription());
        assertThat(actualAction.getNextState()).isEqualTo(expectedAction.getNextState());
        assertThat(actualAction.getUpdatedAt()).isEqualTo(expectedAction.getUpdatedAt());
        assertThat(actualAction.getUpdatedBy()).isEqualTo(expectedAction.getUpdatedBy());
        assertThat(actualAction.getVersion()).isEqualTo(expectedAction.getVersion());
    }

    @Test
    public void pojo_methods_are_well_implemented() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(Action.class, FieldPredicate.exclude("code")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(Action.class).
                testing(Method.CONSTRUCTOR, Method.GETTER, Method.TO_STRING).areWellImplemented();
    }

    @Test
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Action.class).
                withPrefabValues(State.class, new State(StateCode.ADDRESS_DISABLED), new State(StateCode.ADDRESS_ENABLED)).
                withOnlyTheseFields(Action.ENTITY_ATTRIBUTE_NAME_CODE).
                verify();
    }
}