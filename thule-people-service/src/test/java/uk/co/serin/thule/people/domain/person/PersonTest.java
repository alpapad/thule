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
    public void builder_and_getters_operate_on_the_same_field() {
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
    public void disable_person() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.disable();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_DISABLED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void disable_person_when_already_disabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.disable();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void discard_person() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.discard();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void discard_person_when_already_discarded() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        person.discard();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void enable_person() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.enable();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void enable_person_when_already_enabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.enable();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void is_expired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfExpiry(LocalDate.MIN).build();

        // When
        boolean expired = person.isExpired();

        //Then
        assertThat(expired).isTrue();
    }

    @Test
    public void is_not_expired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfExpiry(LocalDate.MAX).build();

        // When
        boolean expired = person.isExpired();

        //Then
        assertThat(expired).isFalse();
    }

    @Test
    public void is_not_password_expired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfPasswordExpiry(LocalDate.MAX).build();

        // When
        boolean passwordExpired = person.isPasswordExpired();

        //Then
        assertThat(passwordExpired).isFalse();
    }

    @Test
    public void is_password_expired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfPasswordExpiry(LocalDate.MIN).build();

        // When
        boolean passwordExpired = person.isPasswordExpired();

        //Then
        assertThat(passwordExpired).isTrue();
    }

    @Test
    public void pojo_methods_are_well_implemented() {
        assertPojoMethodsFor(Person.class, FieldPredicate.exclude("photographs", "roles", "userId")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(Person.class, FieldPredicate.exclude("homeAddress", "password", "photographs", "roles", "workAddress")).
                testing(Method.TO_STRING).areWellImplemented();

        assertPojoMethodsFor(Person.class).
                testing(Method.CONSTRUCTOR, Method.GETTER).areWellImplemented();
    }

    @Test
    public void recover_person() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        person.recover();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test
    public void update_person() {
        // Given
        Person expectedPerson = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();
        Person actualPerson = testDataFactory.buildPerson(expectedPerson);

        // When
        actualPerson.update();

        // Then
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Test(expected = PersonInvalidStateException.class)
    public void update_person_when_not_enabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.update();

        // Then (see expected in @Test annotation)
    }

    @Test(expected = PersonInvalidStateException.class)
    public void update_recover_when_not_discarded() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.recover();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void validation_fails_when_userid_is_empty() {
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

    @Test
    public void validation_when_all_fields_are_valid() {
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
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Person.class).
                withPrefabValues(Action.class, new Action(ActionCode.ADDRESS_DISABLE), new Action(ActionCode.ADDRESS_DISCARD)).
                withPrefabValues(Person.class, new Person("userid"), new Person("another-userid")).
                withRedefinedSuperclass().
                withOnlyTheseFields(Person.ENTITY_ATTRIBUTE_NAME_USER_ID).
                verify();
    }
}