package uk.co.serin.thule.people.domain.state;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.MockReferenceDataFactory;
import uk.co.serin.thule.people.datafactories.ReferenceDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionTest {
    private ReferenceDataFactory referenceDataFactory = new MockReferenceDataFactory();

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given

        // When
        Action action = new Action(ActionCode.ADDRESS_DISABLE);

        // Then
        assertThat(action.getCode()).isEqualTo(ActionCode.ADDRESS_DISABLE);
    }

    @Test
    public void copyConstructorCreatesInstanceWithSameFieldValues() {
        // Given
        Action expectedAction = referenceDataFactory.getActions().get(ActionCode.ADDRESS_UPDATE);

        // When
        Action actualAction = new Action(expectedAction);

        // Then
        assertThat(actualAction).isEqualToComparingFieldByField(expectedAction);
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        Action action = new Action();

        // Then
        assertThat(action).isNotNull();
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        String description = "description";
        State nextState = referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED);

        Action action = new Action(ActionCode.PERSON_ENABLE).setDescription(description).setNextState(nextState);

        // When/Then
        assertThat(action.getCode()).isEqualTo(ActionCode.PERSON_ENABLE);
        assertThat(action.getDescription()).isEqualTo(description);
        assertThat(action.getNextState()).isEqualTo(nextState);
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new Action(ActionCode.ADDRESS_ENABLE).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_CODE);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Action.class).
                withPrefabValues(State.class, new State(StateCode.ADDRESS_DISABLED), new State(StateCode.ADDRESS_ENABLED)).
                suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}