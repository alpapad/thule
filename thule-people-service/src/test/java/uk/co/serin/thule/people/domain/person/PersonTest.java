package uk.co.serin.thule.people.domain.person;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.MockReferenceDataFactory;
import uk.co.serin.thule.people.datafactories.ReferenceDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.address.HomeAddress;
import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonTest {
    private ReferenceDataFactory referenceDataFactory = new MockReferenceDataFactory();

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
        LocalDate now = LocalDate.now();
        Country country = referenceDataFactory.getCountries().get(Country.GBR);
        HomeAddress homeAddress = new HomeAddress("addressLine1", "postCode", country);
        WorkAddress workAddress = new WorkAddress("addressLine1", "postCode", country);

        Person expectedPerson = new Person("userId");
        expectedPerson.setDateOfBirth(now);
        expectedPerson.setDateOfExpiry(now);
        expectedPerson.setDateOfPasswordExpiry(now);
        expectedPerson.setEmailAddress("test@gmail.com");
        expectedPerson.setFirstName("firstName");
        expectedPerson.setHomeAddress(homeAddress);
        expectedPerson.setPassword("password");
        expectedPerson.addPhotographs(Stream.of(new Photograph(new byte[]{}, expectedPerson)));
        expectedPerson.addRoles(referenceDataFactory.getRoles().values().stream());
        expectedPerson.setSalutation("salutation");
        expectedPerson.setSecondName("secondName");
        expectedPerson.setState(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED));
        expectedPerson.setSurname("surname");
        expectedPerson.setWorkAddress(workAddress);

        // When
        Person actualPerson = new Person(expectedPerson);

        // Then
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
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
        Person person = new Person("userId");
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));

        State expectedState = referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED);

        // When
        person.disable();

        //Then
        assertThat(person.getState()).isEqualTo(expectedState);
    }

    @Test(expected = PersonInvalidStateException.class)
    public void disablePersonWhenAlreadyDisabled() {
        // Given
        Person person = new Person("userId");
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED));

        // When
        person.disable();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void discardPerson() {
        // Given
        Person person = new Person("userId");
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));

        State expectedState = referenceDataFactory.getStates().get(StateCode.PERSON_DISCARDED);

        // When
        person.discard();

        //Then
        assertThat(person.getState()).isEqualTo(expectedState);
    }

    @Test(expected = PersonInvalidStateException.class)
    public void discardPersonWhenAlreadyDiscarded() {
        // Given
        Person person = new Person("userId");
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_DISCARDED));

        // When
        person.discard();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void enablePerson() {
        // Given
        Person person = new Person("userId");
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED));

        State expectedState = referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED);

        // When
        person.enable();

        //Then
        assertThat(person.getState()).isEqualTo(expectedState);
    }

    @Test(expected = PersonInvalidStateException.class)
    public void enablePersonWhenAlreadyEnabled() {
        // Given
        Person person = new Person("userId");
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));

        // When
        person.enable();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        LocalDate now = LocalDate.now();
        Country country = referenceDataFactory.getCountries().get(Country.GBR);
        String emailAddress = "test@gmail.com";
        String firstName = "firstName";
        HomeAddress homeAddress = new HomeAddress("addressLine1", "postCode", country);
        String password = "password";
        String salutation = "salutation";
        String secondName = "secondName";
        State state = referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED);
        String surname = "surname";
        String userId = "userId";
        WorkAddress workAddress = new WorkAddress("addressLine1", "postCode", country);

        Person person = new Person(userId);
        person.setDateOfBirth(now);
        person.setDateOfExpiry(now);
        person.setDateOfPasswordExpiry(now);
        person.setEmailAddress(emailAddress);
        person.setFirstName(firstName);
        person.setHomeAddress(homeAddress);
        person.setPassword(password);
        person.setSalutation(salutation);
        person.setSecondName(secondName);
        person.setState(state);
        person.setSurname(surname);
        person.setWorkAddress(workAddress);

        // When/Then
        assertThat(person.getDateOfBirth()).isEqualTo(now);
        assertThat(person.getDateOfExpiry()).isEqualTo(now);
        assertThat(person.getDateOfPasswordExpiry()).isEqualTo(now);
        assertThat(person.getEmailAddress()).isEqualTo(emailAddress);
        assertThat(person.getFirstName()).isEqualTo(firstName);
        assertThat(person.getHomeAddress()).isEqualTo(homeAddress);
        assertThat(person.getPassword()).isEqualTo(password);
        assertThat(person.getSalutation()).isEqualTo(salutation);
        assertThat(person.getSecondName()).isEqualTo(secondName);
        assertThat(person.getState()).isEqualTo(state);
        assertThat(person.getSurname()).isEqualTo(surname);
        assertThat(person.getWorkAddress()).isEqualTo(workAddress);
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
    public void recoverPerson() {
        // Given
        Person person = new Person("userId");
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_DISCARDED));

        State expectedState = referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED);

        // When
        person.recover();

        //Then
        assertThat(person.getState()).isEqualTo(expectedState);
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new Person("userId").toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_USER_ID);
    }

    @Test
    public void updatePerson() {
        // Given
        Person expectedPerson = new Person("userId");
        expectedPerson.setState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));

        Person actualPerson = new Person(expectedPerson);

        // When
        actualPerson.update();

        // Then
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Test(expected = PersonInvalidStateException.class)
    public void updatePersonWhenNotEnabled() {
        // Given
        Person person = new Person("userId");
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED));

        // When
        person.update();

        // Then (see expected in @Test annotation)
    }

    @Test(expected = PersonInvalidStateException.class)
    public void updateRecoverWhenNotDiscarded() {
        // Given
        Person person = new Person("userId");
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));

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
                suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}