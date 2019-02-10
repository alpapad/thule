package uk.co.serin.thule.people.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.people.datafactory.MockReferenceDataFactory;
import uk.co.serin.thule.people.datafactory.ReferenceDataFactory;
import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.entity.person.PersonInvalidStateException;
import uk.co.serin.thule.people.domain.model.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.PersonRepositoryImpl;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@RunWith(MockitoJUnitRunner.class)
public class PeopleServiceTest {
    @InjectMocks
    private PeopleService peopleService;
    private ReferenceDataFactory referenceDataFactory = new MockReferenceDataFactory();

    @Test
    public void when_disable_person_then_state_is_updated_to_person_disabled() {
        // Given
        var person = PersonEntity.builder().userId("userId").state(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        peopleService.disable(person);

        //Then
        assertThat(person.getState()).isEqualTo(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED));
    }

    @Test
    public void when_disabling_person_already_disabled_then_person_invalid_state_exception_is_thrown() {
        // Given
        var person = PersonEntity.builder().userId("userId").state(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        var personInvalidStateException = catchThrowableOfType(() -> peopleService.disable(person), PersonInvalidStateException.class);

        // Then
        assertThat(personInvalidStateException).isNotNull();
    }

    @Test
    public void when_discard_person_then_state_is_updated_to_person_discarded() {
        // Given
        var person = PersonEntity.builder().userId("userId").state(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        peopleService.discard(person);

        //Then
        assertThat(person.getState()).isEqualTo(referenceDataFactory.getStates().get(StateCode.PERSON_DISCARDED));
    }

    @Test
    public void when_discarding_person_already_discarded_then_person_invalid_state_exception_is_thrown() {
        // Given
        var person = PersonEntity.builder().userId("userId").state(referenceDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        var personInvalidStateException = catchThrowableOfType(() -> peopleService.discard(person), PersonInvalidStateException.class);

        // Then
        assertThat(personInvalidStateException).isNotNull();
    }

    @Test
    public void when_enable_person_already_enabled_then_person_invalid_state_exception_is_thrown() {
        // Given
        var person = PersonEntity.builder().userId("userId").state(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        var personInvalidStateException = catchThrowableOfType(() -> peopleService.enable(person), PersonInvalidStateException.class);

        // Then
        assertThat(personInvalidStateException).isNotNull();
    }

    @Test
    public void when_enable_person_then_state_is_updated_to_person_enabled() {
        // Given
        var person = PersonEntity.builder().userId("userId").state(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        peopleService.enable(person);

        //Then
        assertThat(person.getState()).isEqualTo(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test
    public void when_expiry_date_is_in_the_future_then_person_is_not_expired() {
        // Given
        var person = PersonEntity.builder().userId("userId").dateOfExpiry(LocalDate.MAX).build();

        // When
        var expired = peopleService.isExpired(person);

        //Then
        assertThat(expired).isFalse();
    }

    @Test
    public void when_expiry_date_is_in_the_past_then_person_is_expired() {
        // Given
        var person = PersonEntity.builder().userId("userId").dateOfExpiry(LocalDate.MIN).build();

        // When
        var expired = peopleService.isExpired(person);

        //Then
        assertThat(expired).isTrue();
    }

    @Test
    public void when_password_expiry_date_is_in_the_future_then_password_is_not_expired() {
        // Given
        var person = PersonEntity.builder().userId("userId").dateOfPasswordExpiry(LocalDate.MAX).build();

        // When
        var passwordExpired = peopleService.isPasswordExpired(person);

        //Then
        assertThat(passwordExpired).isFalse();
    }

    @Test
    public void when_password_expiry_date_is_in_the_past_then_password_is_expired() {
        // Given
        var person = PersonEntity.builder().userId("userId").dateOfPasswordExpiry(LocalDate.MIN).build();

        // When
        var passwordExpired = peopleService.isPasswordExpired(person);

        //Then
        assertThat(passwordExpired).isTrue();
    }

    @Test
    public void when_person_contains_invalid_fields_then_validation_fails() {
        // Given
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var person = PersonEntity.builder().build();

        // When
        var constraintViolations = validator.validate(person);

        // Then
        var constraintViolationsByProperty = constraintViolations.stream().collect(
                Collectors.toMap(constraintViolation -> constraintViolation.getPropertyPath().toString(), Function.identity()));

        assertThat(constraintViolationsByProperty.containsKey(PersonRepositoryImpl.ENTITY_ATTRIBUTE_NAME_USER_ID)).isTrue();
    }

    @Test
    public void when_recover_person_then_state_is_changed_to_person_enabled() {
        // Given
        var person = PersonEntity.builder().userId("userId").state(referenceDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        peopleService.recover(person);

        //Then
        assertThat(person.getState()).isEqualTo(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test
    public void when_recovering_an_enabled_person_then_person_invalid_state_exception_is_thrown() {
        // Given
        var person = PersonEntity.builder().userId("userId").state(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        var personInvalidStateException = catchThrowableOfType(() -> peopleService.recover(person), PersonInvalidStateException.class);

        // Then
        assertThat(personInvalidStateException).isNotNull();
    }

    @Test
    public void when_update_person_then_state_remains_unchanged() {
        // Given
        var statePersonEnabled = referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED);
        var person = PersonEntity.builder().userId("userId").state(statePersonEnabled).build();

        // When
        peopleService.update(person);

        // Then
        assertThat(person.getState()).isEqualTo(statePersonEnabled);
    }

    @Test
    public void when_updating_a_disabled_person_then_person_invalid_state_exception_is_thrown() {
        // Given
        var person = PersonEntity.builder().userId("userId").state(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        var personInvalidStateException = catchThrowableOfType(() -> peopleService.update(person), PersonInvalidStateException.class);

        // Then
        assertThat(personInvalidStateException).isNotNull();
    }
}