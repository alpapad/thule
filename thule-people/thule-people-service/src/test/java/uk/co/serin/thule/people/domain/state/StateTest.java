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
    public void when_builder_method_then_getters_operate_on_the_same_field() {
        // Given
        State expectedState = testDataFactory.getStates().get(StateCode.PERSON_ENABLED);

        // When
        State actualState = State.StateBuilder.aState().
                withCode(expectedState.getCode()).
                withDescription(expectedState.getDescription()).
                build();

        // Then
        assertThat(actualState.getCode()).isEqualTo(expectedState.getCode());
        assertThat(actualState.getCreatedAt()).isEqualTo(expectedState.getCreatedAt());
        assertThat(actualState.getCreatedBy()).isEqualTo(expectedState.getCreatedBy());
        assertThat(actualState.getId()).isEqualTo(expectedState.getId());
        assertThat(actualState.getDescription()).isEqualTo(expectedState.getDescription());
        assertThat(actualState.getUpdatedAt()).isEqualTo(expectedState.getUpdatedAt());
        assertThat(actualState.getUpdatedBy()).isEqualTo(expectedState.getUpdatedBy());
        assertThat(actualState.getVersion()).isEqualTo(expectedState.getVersion());
    }

    @Test
    public void when_equals_is_overridden_then_verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(State.class).
                withPrefabValues(State.class, new State(StateCode.ADDRESS_DISABLED), new State(StateCode.ADDRESS_ENABLED)).
                withOnlyTheseFields(State.ENTITY_ATTRIBUTE_NAME_CODE).
                verify();
    }

    @Test
    public void when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
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
}