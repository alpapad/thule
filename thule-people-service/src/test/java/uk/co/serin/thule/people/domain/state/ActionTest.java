package uk.co.serin.thule.people.domain.state;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builderAndSettersOperateOnTheSameField() {
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
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given

        // When
        Action action = new Action(ActionCode.ADDRESS_DISABLE);

        // Then
        assertThat(action.getCode()).isEqualTo(ActionCode.ADDRESS_DISABLE);
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
        Action expectedAction = testDataFactory.getActions().get(ActionCode.PERSON_ENABLE);

        // When
        Action actualAction = new Action(expectedAction.getCode());
        actualAction.setDescription(expectedAction.getDescription());
        actualAction.setNextState(expectedAction.getNextState());

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
    public void toStringIsOverridden() {
        assertThat(new Action(ActionCode.ADDRESS_ENABLE).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_CODE);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Action.class).
                withPrefabValues(State.class, new State(StateCode.ADDRESS_DISABLED), new State(StateCode.ADDRESS_ENABLED)).
                withOnlyTheseFields(Action.ENTITY_ATTRIBUTE_NAME_CODE).
                verify();
    }
}