package uk.co.serin.thule.people.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.entity.state.ActionEntity;
import uk.co.serin.thule.people.domain.entity.state.StateEntity;
import uk.co.serin.thule.people.domain.model.state.ActionCode;
import uk.co.serin.thule.people.domain.model.state.StateCode;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@ExtendWith(MockitoExtension.class)
class PeopleServiceTest {
    @InjectMocks
    private PeopleService peopleService;

    @Test
    void when_disable_person_then_state_is_updated_to_person_disabled() {
        // Given
        var nextState = StateEntity.builder().code(StateCode.PERSON_DISABLED).build();
        var allowedAction = ActionEntity.builder().code(ActionCode.PERSON_DISABLE).nextState(nextState).build();
        var state = StateEntity.builder().actions(Set.of(allowedAction)).build();
        var person = PersonEntity.builder().state(state).build();

        // When
        peopleService.disable(person);

        // Then
        assertThat(person.getState()).isEqualTo(nextState);
    }

    @Test
    void when_disabling_person_already_disabled_then_person_invalid_state_exception_is_thrown() {
        // Given
        var allowedAction = ActionEntity.builder().code(ActionCode.PERSON_ENABLE).build();
        var state = StateEntity.builder().code(StateCode.PERSON_DISABLED).actions(Set.of(allowedAction)).build();
        var person = PersonEntity.builder().state(state).build();

        // When
        var personInvalidStateException = catchThrowableOfType(() -> peopleService.disable(person), PersonInvalidStateException.class);

        // Then
        assertThat(personInvalidStateException).isNotNull();
    }

    @Test
    void when_discard_person_then_state_is_updated_to_person_discarded() {
        // Given
        var nextState = StateEntity.builder().code(StateCode.PERSON_DISCARDED).build();
        var allowedAction = ActionEntity.builder().code(ActionCode.PERSON_DISCARD).nextState(nextState).build();
        var state = StateEntity.builder().actions(Set.of(allowedAction)).build();
        var person = PersonEntity.builder().state(state).build();

        // When
        peopleService.discard(person);

        // Then
        assertThat(person.getState()).isEqualTo(nextState);
    }

    @Test
    void when_discarding_person_already_discarded_then_person_invalid_state_exception_is_thrown() {
        // Given
        var allowedAction = ActionEntity.builder().code(ActionCode.PERSON_ENABLE).build();
        var state = StateEntity.builder().code(StateCode.PERSON_DISCARDED).actions(Set.of(allowedAction)).build();
        var person = PersonEntity.builder().state(state).build();

        // When
        var personInvalidStateException = catchThrowableOfType(() -> peopleService.discard(person), PersonInvalidStateException.class);

        // Then
        assertThat(personInvalidStateException).isNotNull();
    }

    @Test
    void when_enable_person_already_enabled_then_person_invalid_state_exception_is_thrown() {
        // Given
        var allowedAction = ActionEntity.builder().code(ActionCode.PERSON_DISABLE).build();
        var state = StateEntity.builder().code(StateCode.PERSON_ENABLED).actions(Set.of(allowedAction)).build();
        var person = PersonEntity.builder().state(state).build();

        // When
        var personInvalidStateException = catchThrowableOfType(() -> peopleService.enable(person), PersonInvalidStateException.class);

        // Then
        assertThat(personInvalidStateException).isNotNull();
    }

    @Test
    void when_enable_person_then_state_is_updated_to_person_enabled() {
        // Given
        var nextState = StateEntity.builder().code(StateCode.PERSON_ENABLED).build();
        var allowedAction = ActionEntity.builder().code(ActionCode.PERSON_ENABLE).nextState(nextState).build();
        var state = StateEntity.builder().actions(Set.of(allowedAction)).build();
        var person = PersonEntity.builder().state(state).build();

        // When
        peopleService.enable(person);

        // Then
        assertThat(person.getState()).isEqualTo(nextState);
    }

    @Test
    void when_expiry_date_is_in_the_future_then_person_is_not_expired() {
        // Given
        var person = PersonEntity.builder().dateOfExpiry(LocalDate.MAX).build();

        // When
        var expired = peopleService.isExpired(person);

        // Then
        assertThat(expired).isFalse();
    }

    @Test
    void when_expiry_date_is_in_the_past_then_person_is_expired() {
        // Given
        var person = PersonEntity.builder().dateOfExpiry(LocalDate.MIN).build();

        // When
        var expired = peopleService.isExpired(person);

        // Then
        assertThat(expired).isTrue();
    }

    @Test
    void when_password_expiry_date_is_in_the_future_then_password_is_not_expired() {
        // Given
        var person = PersonEntity.builder().dateOfPasswordExpiry(LocalDate.MAX).build();

        // When
        var passwordExpired = peopleService.isPasswordExpired(person);

        // Then
        assertThat(passwordExpired).isFalse();
    }

    @Test
    void when_password_expiry_date_is_in_the_past_then_password_is_expired() {
        // Given
        var person = PersonEntity.builder().dateOfPasswordExpiry(LocalDate.MIN).build();

        // When
        var passwordExpired = peopleService.isPasswordExpired(person);

        // Then
        assertThat(passwordExpired).isTrue();
    }

    @Test
    void when_person_contains_invalid_fields_then_validation_fails() {
        // Given
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var person = PersonEntity.builder().build();

        // When
        var constraintViolations = validator.validate(person);

        // Then
        var constraintViolationsByProperty = constraintViolations.stream().collect(
                Collectors.toMap(constraintViolation -> constraintViolation.getPropertyPath().toString(), Function.identity()));

        assertThat(constraintViolationsByProperty).containsKey("userId");
    }

    @Test
    void when_recover_person_then_state_is_changed_to_person_enabled() {
        // Given
        var nextState = StateEntity.builder().code(StateCode.PERSON_ENABLED).build();
        var allowedAction = ActionEntity.builder().code(ActionCode.PERSON_RECOVER).nextState(nextState).build();
        var state = StateEntity.builder().actions(Set.of(allowedAction)).build();
        var person = PersonEntity.builder().state(state).build();

        // When
        peopleService.recover(person);

        // Then
        assertThat(person.getState()).isEqualTo(nextState);
    }

    @Test
    void when_recovering_an_enabled_person_then_person_invalid_state_exception_is_thrown() {
        // Given
        var allowedAction = ActionEntity.builder().code(ActionCode.PERSON_ENABLE).build();
        var state = StateEntity.builder().code(StateCode.PERSON_ENABLED).actions(Set.of(allowedAction)).build();
        var person = PersonEntity.builder().state(state).build();

        // When
        var personInvalidStateException = catchThrowableOfType(() -> peopleService.recover(person), PersonInvalidStateException.class);

        // Then
        assertThat(personInvalidStateException).isNotNull();
    }
}