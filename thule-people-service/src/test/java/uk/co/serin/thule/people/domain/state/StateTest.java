package uk.co.serin.thule.people.domain.state;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

public class StateTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builderAndSettersOperateOnTheSameField() {
        // Given
        State expectedState = testDataFactory.getStates().get(StateCode.PERSON_ENABLED);

        // When
        State actualState = State.StateBuilder.aState().
                withCode(expectedState.getCode()).
                withCreatedAt(expectedState.getCreatedAt()).
                withDescription(expectedState.getDescription()).
                withId(expectedState.getId()).
                withUpdatedAt(expectedState.getUpdatedAt()).
                withUpdatedBy(expectedState.getUpdatedBy()).
                withVersion(expectedState.getVersion()).
                build();

        // Then
        assertThat(actualState.getCode()).isEqualTo(expectedState.getCode());
        assertThat(actualState.getCreatedAt()).isEqualTo(expectedState.getCreatedAt());
        assertThat(actualState.getId()).isEqualTo(expectedState.getId());
        assertThat(actualState.getDescription()).isEqualTo(expectedState.getDescription());
        assertThat(actualState.getUpdatedAt()).isEqualTo(expectedState.getUpdatedAt());
        assertThat(actualState.getUpdatedBy()).isEqualTo(expectedState.getUpdatedBy());
        assertThat(actualState.getVersion()).isEqualTo(expectedState.getVersion());
    }

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given

        // When
        State actualState = new State(StateCode.ADDRESS_ENABLED);

        // Then
        assertThat(actualState.getCode()).isEqualTo(StateCode.ADDRESS_ENABLED);
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        State actualState = new State();

        // Then
        assertThat(actualState).isNotNull();
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        State expectedState = testDataFactory.getStates().get(StateCode.PERSON_ENABLED);

        // When
        State actualState = new State(expectedState.getCode());
        actualState.setDescription(expectedState.getDescription());

        // Then
        assertThat(actualState.getCode()).isEqualTo(expectedState.getCode());
        assertThat(actualState.getCreatedAt()).isEqualTo(expectedState.getCreatedAt());
        assertThat(actualState.getId()).isEqualTo(expectedState.getId());
        assertThat(actualState.getDescription()).isEqualTo(expectedState.getDescription());
        assertThat(actualState.getUpdatedAt()).isEqualTo(expectedState.getUpdatedAt());
        assertThat(actualState.getUpdatedBy()).isEqualTo(expectedState.getUpdatedBy());
        assertThat(actualState.getVersion()).isEqualTo(expectedState.getVersion());
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new State(StateCode.ADDRESS_DISCARDED).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_CODE);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(State.class).
                withPrefabValues(State.class, new State(StateCode.ADDRESS_DISABLED), new State(StateCode.ADDRESS_ENABLED)).
                withOnlyTheseFields(State.ENTITY_ATTRIBUTE_NAME_CODE).
                verify();
    }
}