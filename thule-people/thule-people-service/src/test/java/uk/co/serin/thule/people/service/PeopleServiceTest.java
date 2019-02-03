package uk.co.serin.thule.people.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.people.datafactory.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.email.Email;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.person.PersonInvalidStateException;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;
import uk.co.serin.thule.people.rest.EmailServiceClient;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PeopleServiceTest {
    private TestDataFactory testDataFactory = new TestDataFactory();
    @Mock
    private EmailServiceClient emailServiceClient;
    @InjectMocks
    private PeopleService peopleService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private StateRepository stateRepository;

    @Test
    public void when_after_create_then_email_notification_is_sent() {
        // Given
        Set<Email> actualEmails = new HashSet<>();
        given(emailServiceClient.create(any(Email.class))).willAnswer(invocation -> {
            Email actualEmail = (Email) invocation.getArguments()[0];
            actualEmails.add(actualEmail);
            return actualEmail;
        });

        // When
        peopleService.afterCreate(Person.builder().userId("userId").build());

        // Then
        Optional<Email> actualEmailOptional = actualEmails.stream().findFirst();
        Email actualEmail = actualEmailOptional.orElseThrow();
        assertThat(actualEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_after_delete_then_email_notification_is_sent() {
        // Given
        Set<Email> actualEmails = new HashSet<>();
        given(emailServiceClient.create(any(Email.class))).willAnswer(invocation -> {
            Email actualEmail = (Email) invocation.getArguments()[0];
            actualEmails.add(actualEmail);
            return actualEmail;
        });

        // When
        peopleService.afterDelete(Person.builder().userId("userId").build());

        // Then
        Optional<Email> actualEmailOptional = actualEmails.stream().findFirst();
        Email actualEmail = actualEmailOptional.orElseThrow();
        assertThat(actualEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_after_save_then_email_notification_is_sent() {
        // Given
        Set<Email> actualEmails = new HashSet<>();
        given(emailServiceClient.create(any(Email.class))).willAnswer(invocation -> {
            Email actualEmail = (Email) invocation.getArguments()[0];
            actualEmails.add(actualEmail);
            return actualEmail;
        });

        // When
        peopleService.afterSave(Person.builder().userId("userId").build());

        // Then
        Optional<Email> actualEmailOptional = actualEmails.stream().findFirst();
        Email actualEmail = actualEmailOptional.orElseThrow();
        assertThat(actualEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_before_create_then_roles_and_state_are_set() {
        // Given
        given(roleRepository.findByCode(any(RoleCode.class))).willReturn(Role.builder().code(RoleCode.ROLE_CLERK).build());
        given(stateRepository.findByCode(any(StateCode.class))).willReturn(State.builder().code(StateCode.PERSON_ENABLED).build());

        // When
        peopleService.beforeCreate(Person.builder().userId("userId").build());

        // Then
        verify(roleRepository).findByCode(any());
        verify(stateRepository).findByCode(any());
    }

    @Test
    public void when_disable_person_then_state_is_person_disabled() {
        // Given
        Person person = Person.builder().userId("userId").state(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        peopleService.disable(person);

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_DISABLED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void when_disabling_person_already_disabled_then_throw_person_invalid_state_exception() {
        // Given
        Person person = Person.builder().userId("userId").state(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        peopleService.disable(person);

        // Then (see expected in @Test annotation)
    }

    @Test
    public void when_discard_person_then_state_is_person_discarded() {
        // Given
        Person person = Person.builder().userId("userId").state(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        peopleService.discard(person);

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void when_discarding_person_already_discarded_then_throw_person_invalid_state_exception() {
        // Given
        Person person = Person.builder().userId("userId").state(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        peopleService.discard(person);

        // Then (see expected in @Test annotation)
    }

    @Test(expected = PersonInvalidStateException.class)
    public void when_enable_person_already_enabled_then_throw_person_invalid_state_exception() {
        // Given
        Person person = Person.builder().userId("userId").state(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        peopleService.enable(person);

        // Then (see expected in @Test annotation)
    }

    @Test
    public void when_enable_person_then_state_is_person_enabled() {
        // Given
        Person person = Person.builder().userId("userId").state(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        peopleService.enable(person);

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test
    public void when_expiry_date_is_in_the_future_then_person_is_not_expired() {
        // Given
        Person person = Person.builder().userId("userId").dateOfExpiry(LocalDate.MAX).build();

        // When
        boolean expired = peopleService.isExpired(person);

        //Then
        assertThat(expired).isFalse();
    }

    @Test
    public void when_expiry_date_is_in_the_past_then_person_is_expired() {
        // Given
        Person person = Person.builder().userId("userId").dateOfExpiry(LocalDate.MIN).build();

        // When
        boolean expired = peopleService.isExpired(person);

        //Then
        assertThat(expired).isTrue();
    }

    @Test
    public void when_password_expiry_date_is_in_the_future_then_password_is_not_expired() {
        // Given
        Person person = Person.builder().userId("userId").dateOfPasswordExpiry(LocalDate.MAX).build();

        // When
        boolean passwordExpired = peopleService.isPasswordExpired(person);

        //Then
        assertThat(passwordExpired).isFalse();
    }

    @Test
    public void when_password_expiry_date_is_in_the_past_then_password_is_expired() {
        // Given
        Person person = Person.builder().userId("userId").dateOfPasswordExpiry(LocalDate.MIN).build();

        // When
        boolean passwordExpired = peopleService.isPasswordExpired(person);

        //Then
        assertThat(passwordExpired).isTrue();
    }

    @Test
    public void when_recover_person_then_state_is_person_enabled() {
        // Given
        Person person = Person.builder().userId("userId").state(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        peopleService.recover(person);

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void when_recovering_an_enabled_person_then_throw_person_invalid_state_exception() {
        // Given
        Person person = Person.builder().userId("userId").state(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        peopleService.recover(person);

        // Then (see expected in @Test annotation)
    }

    @Test
    public void when_update_person_then_state_remains_unchanged() {
        // Given
        Person expectedPerson = Person.builder().userId("userId").state(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();
        Person actualPerson = testDataFactory.buildPerson(expectedPerson);

        // When
        peopleService.update(actualPerson);

        // Then
        assertThat(actualPerson.getState()).isEqualTo(expectedPerson.getState());
    }

    @Test(expected = PersonInvalidStateException.class)
    public void when_updating_a_disabled_person_then_throw_person_invalid_state_exception() {
        // Given
        Person person = Person.builder().userId("userId").state(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        peopleService.update(person);

        // Then (see expected in @Test annotation)
    }

    @Test
    public void when_userid_is_empty_then_validation_fails() {
        // Given
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Person person = testDataFactory.buildPersonWithAllAssociations();
        ReflectionTestUtils.setField(person, "userId", "");

        // When
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        // Then
        Map<String, ConstraintViolation<Person>> constraintViolationsByProperty = constraintViolations.stream().collect(
                Collectors.toMap(constraintViolation -> constraintViolation.getPropertyPath().toString(), Function.identity()));

        assertThat(constraintViolationsByProperty.containsKey(DomainModel.ENTITY_ATTRIBUTE_NAME_USER_ID)).isTrue();
    }
}