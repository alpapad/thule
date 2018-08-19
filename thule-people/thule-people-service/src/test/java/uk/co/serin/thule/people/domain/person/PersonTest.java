package uk.co.serin.thule.people.domain.person;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import uk.co.serin.thule.people.datafactory.TestDataFactory;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.StateCode;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class PersonTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void when_all_fields_are_valid_then_validation_succeeds() {
        // Given
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Person person = testDataFactory.buildPersonWithAllAssociations();

        // When
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        // Then
        assertThat(constraintViolations).hasSize(0);
    }

    @Test
    public void when_builder_method_then_getters_operate_on_the_same_field() {
        // Given
        Person expectedPerson = testDataFactory.buildPersonWithAllAssociations();

        // When
        Person actualPerson = Person.PersonBuilder.aPerson().
                withCreatedAt(expectedPerson.getCreatedAt()).
                withDateOfBirth(expectedPerson.getDateOfBirth()).
                withDateOfExpiry(expectedPerson.getDateOfExpiry()).
                withDateOfPasswordExpiry(expectedPerson.getDateOfPasswordExpiry()).
                withEmailAddress(expectedPerson.getEmailAddress()).
                withFirstName(expectedPerson.getFirstName()).
                withHomeAddress(expectedPerson.getHomeAddress()).
                withId(expectedPerson.getId()).
                withLastName(expectedPerson.getLastName()).
                withPassword(expectedPerson.getPassword()).
                withPhotographs(expectedPerson.getPhotographs()).
                withRoles(expectedPerson.getRoles()).
                withSecondName(expectedPerson.getSecondName()).
                withState(expectedPerson.getState()).
                withTitle(expectedPerson.getTitle()).
                withUpdatedAt(expectedPerson.getUpdatedAt()).
                withUpdatedBy(expectedPerson.getUpdatedBy()).
                withUserId(expectedPerson.getUserId()).
                withVersion(expectedPerson.getVersion()).
                withWorkAddress(expectedPerson.getWorkAddress()).build();

        // Then
        assertThat(actualPerson.getCreatedAt()).isEqualTo(expectedPerson.getCreatedAt());
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(expectedPerson.getDateOfBirth());
        assertThat(actualPerson.getDateOfExpiry()).isEqualTo(expectedPerson.getDateOfExpiry());
        assertThat(actualPerson.getDateOfPasswordExpiry()).isEqualTo(expectedPerson.getDateOfPasswordExpiry());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(expectedPerson.getEmailAddress());
        assertThat(actualPerson.getFirstName()).isEqualTo(expectedPerson.getFirstName());
        assertThat(actualPerson.getHomeAddress()).isEqualTo(expectedPerson.getHomeAddress());
        assertThat(actualPerson.getId()).isEqualTo(expectedPerson.getId());
        assertThat(actualPerson.getLastName()).isEqualTo(expectedPerson.getLastName());
        assertThat(actualPerson.getPassword()).isEqualTo(expectedPerson.getPassword());
        assertThat(actualPerson.getPhotographs()).isEqualTo(expectedPerson.getPhotographs());
        assertThat(actualPerson.getRoles()).isEqualTo(expectedPerson.getRoles());
        assertThat(actualPerson.getSecondName()).isEqualTo(expectedPerson.getSecondName());
        assertThat(actualPerson.getState()).isEqualTo(expectedPerson.getState());
        assertThat(actualPerson.getTitle()).isEqualTo(expectedPerson.getTitle());
        assertThat(actualPerson.getUpdatedAt()).isEqualTo(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(expectedPerson.getUpdatedBy());
        assertThat(actualPerson.getVersion()).isEqualTo(expectedPerson.getVersion());
        assertThat(actualPerson.getWorkAddress()).isEqualTo(expectedPerson.getWorkAddress());
    }

    @Test
    public void when_disable_person_then_state_is_person_disabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.disable();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_DISABLED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void when_disabling_person_already_disabled_then_throw_person_invalid_state_exception() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.disable();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void when_discard_person_then_state_is_person_discarded() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.discard();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void when_discarding_person_already_discarded_then_throw_person_invalid_state_exception() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        person.discard();

        // Then (see expected in @Test annotation)
    }

    @Test(expected = PersonInvalidStateException.class)
    public void when_enable_person_already_enabled_then_throw_person_invalid_state_exception() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.enable();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void when_enable_person_then_state_is_person_enabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.enable();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test
    public void when_equals_is_overiiden_then_verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Person.class).
                withPrefabValues(Action.class, new Action(ActionCode.ADDRESS_DISABLE), new Action(ActionCode.ADDRESS_DISCARD)).
                withPrefabValues(Person.class, new Person("userid"), new Person("another-userid")).
                withRedefinedSuperclass().
                withOnlyTheseFields(Person.ENTITY_ATTRIBUTE_NAME_USER_ID).
                verify();
    }

    @Test
    public void when_expiry_date_is_in_the_future_then_person_is_not_expired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfExpiry(LocalDate.MAX).build();

        // When
        boolean expired = person.isExpired();

        //Then
        assertThat(expired).isFalse();
    }

    @Test
    public void when_expiry_date_is_in_the_past_then_person_is_expired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfExpiry(LocalDate.MIN).build();

        // When
        boolean expired = person.isExpired();

        //Then
        assertThat(expired).isTrue();
    }

    @Test
    public void when_password_expiry_date_is_in_the_future_then_password_is_not_expired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfPasswordExpiry(LocalDate.MAX).build();

        // When
        boolean passwordExpired = person.isPasswordExpired();

        //Then
        assertThat(passwordExpired).isFalse();
    }

    @Test
    public void when_password_expiry_date_is_in_the_past_then_password_is_expired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfPasswordExpiry(LocalDate.MIN).build();

        // When
        boolean passwordExpired = person.isPasswordExpired();

        //Then
        assertThat(passwordExpired).isTrue();
    }

    @Test
    public void when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
        assertPojoMethodsFor(Person.class, FieldPredicate.exclude("photographs", "roles", "userId")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(Person.class, FieldPredicate.exclude("homeAddress", "password", "photographs", "roles", "workAddress")).
                testing(Method.TO_STRING).areWellImplemented();

        assertPojoMethodsFor(Person.class).
                testing(Method.CONSTRUCTOR, Method.GETTER).areWellImplemented();
    }

    @Test
    public void when_recover_person_then_state_is_person_enabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        person.recover();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void when_recovering_an_enabled_person_then_throw_person_invalid_state_exception() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.recover();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void when_update_person_then_state_remains_unchanged() {
        // Given
        Person expectedPerson = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();
        Person actualPerson = testDataFactory.buildPerson(expectedPerson);

        // When
        actualPerson.update();

        // Then
        assertThat(actualPerson.getState()).isEqualTo(expectedPerson.getState());
    }

    @Test(expected = PersonInvalidStateException.class)
    public void when_updating_a_disabled_person_then_throw_person_invalid_state_exception() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.update();

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
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo("must not be empty");
    }
}