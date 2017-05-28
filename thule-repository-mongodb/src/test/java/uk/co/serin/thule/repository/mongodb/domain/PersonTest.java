package uk.co.serin.thule.repository.mongodb.domain;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonTest {
    @Test
    public void builderAndGettersOperateOnTheSameField() {
        // Given
        Person expectedPerson = PersonFactory.newPerson();

        // When
        Person actualPerson = Person.PersonBuilder.aPerson().
                withCreatedAt(expectedPerson.getCreatedAt()).
                withDateOfBirth(expectedPerson.getDateOfBirth()).
                withDateOfExpiry(expectedPerson.getDateOfExpiry()).
                withDateOfPasswordExpiry(expectedPerson.getDateOfPasswordExpiry()).
                withEmailAddress(expectedPerson.getEmailAddress()).
                withFirstName(expectedPerson.getFirstName()).
                withId(expectedPerson.getId()).
                withPassword(expectedPerson.getPassword()).
                withSalutation(expectedPerson.getSalutation()).
                withSecondName(expectedPerson.getSecondName()).
                withSurname(expectedPerson.getSurname()).
                withUpdatedAt(expectedPerson.getUpdatedAt()).
                withUpdatedBy(expectedPerson.getUpdatedBy()).
                withUserId(expectedPerson.getUserId()).
                withVersion(expectedPerson.getVersion()).build();

        // Then
        assertThat(actualPerson.getCreatedAt()).isEqualTo(expectedPerson.getCreatedAt());
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(expectedPerson.getDateOfBirth());
        assertThat(actualPerson.getDateOfExpiry()).isEqualTo(expectedPerson.getDateOfExpiry());
        assertThat(actualPerson.getDateOfPasswordExpiry()).isEqualTo(expectedPerson.getDateOfPasswordExpiry());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(expectedPerson.getEmailAddress());
        assertThat(actualPerson.getFirstName()).isEqualTo(expectedPerson.getFirstName());
        assertThat(actualPerson.getId()).isEqualTo(expectedPerson.getId());
        assertThat(actualPerson.getPassword()).isEqualTo(expectedPerson.getPassword());
        assertThat(actualPerson.getSalutation()).isEqualTo(expectedPerson.getSalutation());
        assertThat(actualPerson.getSecondName()).isEqualTo(expectedPerson.getSecondName());
        assertThat(actualPerson.getSurname()).isEqualTo(expectedPerson.getSurname());
        assertThat(actualPerson.getUpdatedAt()).isEqualTo(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(expectedPerson.getUpdatedBy());
        assertThat(actualPerson.getVersion()).isEqualTo(expectedPerson.getVersion());
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
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        Person expectedPerson = PersonFactory.newPerson();

        // When
        Person actualPerson = new Person(expectedPerson.getUserId());
        actualPerson.setCreatedAt(expectedPerson.getCreatedAt());
        actualPerson.setDateOfBirth(expectedPerson.getDateOfBirth());
        actualPerson.setDateOfExpiry(expectedPerson.getDateOfExpiry());
        actualPerson.setDateOfPasswordExpiry(expectedPerson.getDateOfPasswordExpiry());
        actualPerson.setEmailAddress(expectedPerson.getEmailAddress());
        actualPerson.setFirstName(expectedPerson.getFirstName());
        actualPerson.setId(expectedPerson.getId());
        actualPerson.setPassword(expectedPerson.getPassword());
        actualPerson.setSalutation(expectedPerson.getSalutation());
        actualPerson.setSecondName(expectedPerson.getSecondName());
        actualPerson.setSurname(expectedPerson.getSurname());
        actualPerson.setUpdatedAt(expectedPerson.getUpdatedAt());
        actualPerson.setUpdatedBy(expectedPerson.getUpdatedBy());
        actualPerson.setVersion(expectedPerson.getVersion());

        // Then
        assertThat(actualPerson.getCreatedAt()).isEqualTo(expectedPerson.getCreatedAt());
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(expectedPerson.getDateOfBirth());
        assertThat(actualPerson.getDateOfExpiry()).isEqualTo(expectedPerson.getDateOfExpiry());
        assertThat(actualPerson.getDateOfPasswordExpiry()).isEqualTo(expectedPerson.getDateOfPasswordExpiry());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(expectedPerson.getEmailAddress());
        assertThat(actualPerson.getFirstName()).isEqualTo(expectedPerson.getFirstName());
        assertThat(actualPerson.getPassword()).isEqualTo(expectedPerson.getPassword());
        assertThat(actualPerson.getSalutation()).isEqualTo(expectedPerson.getSalutation());
        assertThat(actualPerson.getSecondName()).isEqualTo(expectedPerson.getSecondName());
        assertThat(actualPerson.getSurname()).isEqualTo(expectedPerson.getSurname());
        assertThat(actualPerson.getUpdatedAt()).isEqualTo(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(expectedPerson.getUpdatedBy());
        assertThat(actualPerson.getVersion()).isEqualTo(expectedPerson.getVersion());
    }

    @Test
    public void isExpired() {
        // Given
        Person person = new Person("userId");
        person.setDateOfExpiry(LocalDate.MIN);

        // When
        boolean expired = person.isExpired();

        //Then
        assertThat(expired).isTrue();
    }

    @Test
    public void isNotExpired() {
        // Given
        Person person = new Person("userId");
        person.setDateOfExpiry(LocalDate.MAX);

        // When
        boolean expired = person.isExpired();

        //Then
        assertThat(expired).isFalse();
    }

    @Test
    public void isNotPasswordExpired() {
        // Given
        Person person = new Person("userId");
        person.setDateOfPasswordExpiry(LocalDate.MAX);

        // When
        boolean passwordExpired = person.isPasswordExpired();

        //Then
        assertThat(passwordExpired).isFalse();
    }

    @Test
    public void isPasswordExpired() {
        // Given
        Person person = new Person("userId");
        person.setDateOfPasswordExpiry(LocalDate.MIN);

        // When
        boolean passwordExpired = person.isPasswordExpired();

        //Then
        assertThat(passwordExpired).isTrue();
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new Person("userId").toString()).contains(Person.ENTITY_ATTRIBUTE_NAME_USER_ID);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Person.class).withOnlyTheseFields(Person.ENTITY_ATTRIBUTE_NAME_USER_ID).verify();
    }
}