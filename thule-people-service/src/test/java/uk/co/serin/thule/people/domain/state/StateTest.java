package uk.co.serin.thule.people.domain.state;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import uk.co.serin.thule.people.datafactory.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class StateTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builder_and_getters_operate_on_the_same_field() {
        // Given
        State expectedState = testDataFactory.getStates().get(StateCode.PERSON_ENABLED);

        // When
        State actualState = State.StateBuilder.aState().
                withCode(expectedState.getCode()).
                withCreatedAt(expectedState.getCreatedAt()).
//                withCreatedBy(expectedState.getCreatedBy()).
                withDescription(expectedState.getDescription()).
                withId(expectedState.getId()).
                withUpdatedAt(expectedState.getUpdatedAt()).
                withUpdatedBy(expectedState.getUpdatedBy()).
                withVersion(expectedState.getVersion()).
                build();

        // Then
        assertThat(actualState.getCode()).isEqualTo(expectedState.getCode());
        assertThat(actualState.getCreatedAt()).isEqualTo(expectedState.getCreatedAt());
//        assertThat(actualState.getCreatedBy()).isEqualTo(expectedState.getCreatedBy());
        assertThat(actualState.getId()).isEqualTo(expectedState.getId());
        assertThat(actualState.getDescription()).isEqualTo(expectedState.getDescription());
        assertThat(actualState.getUpdatedAt()).isEqualTo(expectedState.getUpdatedAt());
        assertThat(actualState.getUpdatedBy()).isEqualTo(expectedState.getUpdatedBy());
        assertThat(actualState.getVersion()).isEqualTo(expectedState.getVersion());
    }

    @Test
    public void pojo_methods_are_well_implemented() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(State.class, FieldPredicate.exclude("code", "actions")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(State.class, FieldPredicate.exclude("actions")).
                testing(Method.TO_STRING).areWellImplemented();

        assertPojoMethodsFor(State.class).
                testing(Method.CONSTRUCTOR, Method.GETTER).areWellImplemented();
    }

    @Test
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(State.class).
                withPrefabValues(State.class, new State(StateCode.ADDRESS_DISABLED), new State(StateCode.ADDRESS_ENABLED)).
                withOnlyTheseFields(State.ENTITY_ATTRIBUTE_NAME_CODE).
                verify();
    }
}