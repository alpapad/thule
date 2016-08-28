package uk.co.serin.thule.repository.mongodb.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.core.utils.RandomGenerators;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonTest {
    private static final String EMAIL_ADDRESS_SUFFIX = "@serin-consultancy.co.uk";
    private static final int SUFFIX_LENGTH = 8;

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
    public void copyConstructorCreatesInstanceWithSameFieldValues() {
        // Given
        Person expectedPerson = newPersonWithAllAssociations();

        // When
        Person actualPerson = new Person(expectedPerson);

        // Then
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    private Person newPersonWithAllAssociations() {
        // Set the attributes
        final LocalDate dob = RandomGenerators.generateUniqueRandomDateInThePast();
        final LocalDate expiryDate = RandomGenerators.generateUniqueRandomDateInTheFuture();
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(SUFFIX_LENGTH);

        Person person = new Person(userId);
        person.setDateOfBirth(dob);
        person.setDateOfExpiry(expiryDate);
        person.setDateOfPasswordExpiry(expiryDate);
        person.setEmailAddress(userId + EMAIL_ADDRESS_SUFFIX);
        person.setFirstName("Elizabeth");
        person.setId(RandomGenerators.generateUniqueRandomLong());
        person.setPassword(userId);
        person.setSalutation("Miss");
        person.setSecondName("K");
        person.setSurname("Scarlett");

        return person;
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
        LocalDate now = LocalDate.now();
        String emailAddress = "test@gmail.com";
        String firstName = "firstName";
        String password = "password";
        String salutation = "salutation";
        String secondName = "secondName";
        String surname = "surname";
        String userId = "userId";

        Person person = new Person(userId);
        person.setDateOfBirth(now);
        person.setDateOfExpiry(now);
        person.setDateOfPasswordExpiry(now);
        person.setEmailAddress(emailAddress);
        person.setFirstName(firstName);
        person.setPassword(password);
        person.setSalutation(salutation);
        person.setSecondName(secondName);
        person.setSurname(surname);

        // When/Then
        assertThat(person.getDateOfBirth()).isEqualTo(now);
        assertThat(person.getDateOfExpiry()).isEqualTo(now);
        assertThat(person.getDateOfPasswordExpiry()).isEqualTo(now);
        assertThat(person.getEmailAddress()).isEqualTo(emailAddress);
        assertThat(person.getFirstName()).isEqualTo(firstName);
        assertThat(person.getPassword()).isEqualTo(password);
        assertThat(person.getSalutation()).isEqualTo(salutation);
        assertThat(person.getSecondName()).isEqualTo(secondName);
        assertThat(person.getSurname()).isEqualTo(surname);
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
    public void shallowcopyConstructorCreatesInstanceWithSameFieldValues() {
        // Given
        Person expectedPerson = new Person("userId");

        // When
        Person actualPerson = new Person(expectedPerson);

        // Then
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new Person("userId").toString()).contains(Person.ENTITY_ATTRIBUTE_NAME_USER_ID);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Person.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}