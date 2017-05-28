package uk.co.serin.thule.people.domain.state;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.MockReferenceDataFactory;
import uk.co.serin.thule.people.datafactories.ReferenceDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

public class StateTest {
    private ReferenceDataFactory referenceDataFactory = new MockReferenceDataFactory();

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
        String description = "description";

        State expectedState = new State(StateCode.PERSON_ENABLED).setDescription(description);

        // When/Then
        assertThat(expectedState.getCode()).isEqualTo(StateCode.PERSON_ENABLED);
        assertThat(expectedState.getDescription()).isEqualTo(description);
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new State(StateCode.ADDRESS_DISCARDED).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_CODE);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(State.class).
                withPrefabValues(State.class, new State(StateCode.ADDRESS_DISABLED), new State(StateCode.ADDRESS_ENABLED)).
                suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}