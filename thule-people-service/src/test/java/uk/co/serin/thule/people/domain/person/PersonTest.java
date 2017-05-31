package uk.co.serin.thule.people.domain.person;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.StateCode;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builderAndGettersOperateOnTheSameField() {
        // Given
        Person expectedPerson = testDataFactory.newPersonWithAllAssociations();

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
                withPassword(expectedPerson.getPassword()).
                withPhotographs(expectedPerson.getPhotographs()).
                withRoles(expectedPerson.getRoles()).
                withSalutation(expectedPerson.getSalutation()).
                withSecondName(expectedPerson.getSecondName()).
                withState(expectedPerson.getState()).
                withSurname(expectedPerson.getSurname()).
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
        assertThat(actualPerson.getPassword()).isEqualTo(expectedPerson.getPassword());
        assertThat(actualPerson.getPhotographs()).isEqualTo(expectedPerson.getPhotographs());
        assertThat(actualPerson.getRoles()).isEqualTo(expectedPerson.getRoles());
        assertThat(actualPerson.getSalutation()).isEqualTo(expectedPerson.getSalutation());
        assertThat(actualPerson.getSecondName()).isEqualTo(expectedPerson.getSecondName());
        assertThat(actualPerson.getState()).isEqualTo(expectedPerson.getState());
        assertThat(actualPerson.getSurname()).isEqualTo(expectedPerson.getSurname());
        assertThat(actualPerson.getUpdatedAt()).isEqualTo(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(expectedPerson.getUpdatedBy());
        assertThat(actualPerson.getVersion()).isEqualTo(expectedPerson.getVersion());
        assertThat(actualPerson.getWorkAddress()).isEqualTo(expectedPerson.getWorkAddress());
    }

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        String userId = "userId";

        // When
        Person person = new Person(userId);

        // Then
        assertThat(person.getDateOfExpiry()).isEqualTo(person.getDateOfPasswordExpiry());
        assertThat(person.getUserId()).isEqualTo(userId);
        assertThat(person.getUserId()).isEqualTo(person.getPassword());
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        Person person = new Person();

        // Then
        assertThat(person).isNotNull();
    }

    @Test
    public void disablePerson() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.disable();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_DISABLED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void disablePersonWhenAlreadyDisabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.disable();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void discardPerson() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.discard();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void discardPersonWhenAlreadyDiscarded() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        person.discard();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void enablePerson() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.enable();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void enablePersonWhenAlreadyEnabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.enable();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        Person expectedPerson = testDataFactory.newPersonWithAllAssociations();

        // When
        Person actualPerson = new Person(expectedPerson.getUserId());
        actualPerson.setDateOfBirth(expectedPerson.getDateOfBirth());
        actualPerson.setDateOfExpiry(expectedPerson.getDateOfExpiry());
        actualPerson.setDateOfPasswordExpiry(expectedPerson.getDateOfPasswordExpiry());
        actualPerson.setEmailAddress(expectedPerson.getEmailAddress());
        actualPerson.setFirstName(expectedPerson.getFirstName());
        actualPerson.setHomeAddress(expectedPerson.getHomeAddress());
        actualPerson.setPassword(expectedPerson.getPassword());
        actualPerson.setSalutation(expectedPerson.getSalutation());
        actualPerson.setSecondName(expectedPerson.getSecondName());
        actualPerson.setState(expectedPerson.getState());
        actualPerson.setSurname(expectedPerson.getSurname());
        actualPerson.setWorkAddress(expectedPerson.getWorkAddress());
        actualPerson.addPhotographs(expectedPerson.getPhotographs());
        actualPerson.addRoles(expectedPerson.getRoles());

        // Then
        assertThat(actualPerson.getCreatedAt()).isEqualTo(expectedPerson.getCreatedAt());
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(expectedPerson.getDateOfBirth());
        assertThat(actualPerson.getDateOfExpiry()).isEqualTo(expectedPerson.getDateOfExpiry());
        assertThat(actualPerson.getDateOfPasswordExpiry()).isEqualTo(expectedPerson.getDateOfPasswordExpiry());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(expectedPerson.getEmailAddress());
        assertThat(actualPerson.getFirstName()).isEqualTo(expectedPerson.getFirstName());
        assertThat(actualPerson.getHomeAddress()).isEqualTo(expectedPerson.getHomeAddress());
        assertThat(actualPerson.getId()).isEqualTo(expectedPerson.getId());
        assertThat(actualPerson.getPassword()).isEqualTo(expectedPerson.getPassword());
        assertThat(actualPerson.getPhotographs()).isEqualTo(expectedPerson.getPhotographs());
        assertThat(actualPerson.getRoles()).isEqualTo(expectedPerson.getRoles());
        assertThat(actualPerson.getSalutation()).isEqualTo(expectedPerson.getSalutation());
        assertThat(actualPerson.getSecondName()).isEqualTo(expectedPerson.getSecondName());
        assertThat(actualPerson.getState()).isEqualTo(expectedPerson.getState());
        assertThat(actualPerson.getSurname()).isEqualTo(expectedPerson.getSurname());
        assertThat(actualPerson.getUpdatedAt()).isEqualTo(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(expectedPerson.getUpdatedBy());
        assertThat(actualPerson.getVersion()).isEqualTo(expectedPerson.getVersion());
        assertThat(actualPerson.getWorkAddress()).isEqualTo(expectedPerson.getWorkAddress());
    }

    @Test
    public void isExpired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfExpiry(LocalDate.MIN).build();

        // When
        boolean expired = person.isExpired();

        //Then
        assertThat(expired).isTrue();
    }

    @Test
    public void isNotExpired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfExpiry(LocalDate.MAX).build();

        // When
        boolean expired = person.isExpired();

        //Then
        assertThat(expired).isFalse();
    }

    @Test
    public void isNotPasswordExpired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfPasswordExpiry(LocalDate.MAX).build();

        // When
        boolean passwordExpired = person.isPasswordExpired();

        //Then
        assertThat(passwordExpired).isFalse();
    }

    @Test
    public void isPasswordExpired() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withDateOfPasswordExpiry(LocalDate.MIN).build();

        // When
        boolean passwordExpired = person.isPasswordExpired();

        //Then
        assertThat(passwordExpired).isTrue();
    }

    @Test
    public void recoverPerson() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        person.recover();

        //Then
        assertThat(person.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new Person("userId").toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_USER_ID);
    }

    @Test
    public void updatePerson() {
        // Given
        Person expectedPerson = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();
        Person actualPerson = testDataFactory.newPerson(expectedPerson);

        // When
        actualPerson.update();

        // Then
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Test(expected = PersonInvalidStateException.class)
    public void updatePersonWhenNotEnabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.update();

        // Then (see expected in @Test annotation)
    }

    @Test(expected = PersonInvalidStateException.class)
    public void updateRecoverWhenNotDiscarded() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.recover();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Person.class).
                withPrefabValues(Action.class, new Action(ActionCode.ADDRESS_DISABLE), new Action(ActionCode.ADDRESS_DISCARD)).
                withPrefabValues(Person.class, new Person("userid"), new Person("another-userid")).
                withRedefinedSuperclass().
                withOnlyTheseFields(Person.ENTITY_ATTRIBUTE_NAME_USER_ID).
                verify();
    }
}