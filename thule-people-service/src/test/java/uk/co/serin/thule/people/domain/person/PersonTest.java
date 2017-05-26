package uk.co.serin.thule.people.domain.person;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.core.utils.RandomGenerators;
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
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonTest {
    private static final String EMAIL_ADDRESS_SUFFIX = "@serin-consultancy.co.uk";
    private static final int SUFFIX_LENGTH = 8;
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

        Person expectedPerson = Person.PersonBuilder.aPerson().withUserId("userId").
                withSalutation("salutation").withFirstName("firstName").withSecondName("secondName").withSurname("surname").
                withHomeAddress(homeAddress).withWorkAddress(workAddress).
                withDateOfBirth(now).withEmailAddress("test@gmail.com").
                withDateOfExpiry(now).withDateOfPasswordExpiry(now).withPassword("password").
                withRoles(new HashSet<>(referenceDataFactory.getRoles().values())).
                withState(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).build();
        expectedPerson.addPhotographs(Stream.of(new Photograph(new byte[]{}, expectedPerson)).collect(Collectors.toSet()));

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
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.disable();

        //Then
        assertThat(person.getState()).isEqualTo(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void disablePersonWhenAlreadyDisabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.disable();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void discardPerson() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.discard();

        //Then
        assertThat(person.getState()).isEqualTo(referenceDataFactory.getStates().get(StateCode.PERSON_DISCARDED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void discardPersonWhenAlreadyDiscarded() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(referenceDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        person.discard();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void enablePerson() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.enable();

        //Then
        assertThat(person.getState()).isEqualTo(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test(expected = PersonInvalidStateException.class)
    public void enablePersonWhenAlreadyEnabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

        // When
        person.enable();

        // Then (see expected in @Test annotation)
    }

    @Test
    public void builderAndGettersOperateOnTheSameField() {
        // Given
        Person expectedPerson = newPersonWithAllAssociations();

        // When
        Person actualPerson = Person.PersonBuilder.aPerson().
                withDateOfBirth(expectedPerson.getDateOfBirth()).
                withDateOfExpiry(expectedPerson.getDateOfExpiry()).
                withDateOfPasswordExpiry(expectedPerson.getDateOfPasswordExpiry()).
                withEmailAddress(expectedPerson.getEmailAddress()).
                withFirstName(expectedPerson.getFirstName()).
                withHomeAddress(expectedPerson.getHomeAddress()).
                withPassword(expectedPerson.getPassword()).
                withPhotographs(expectedPerson.getPhotographs()).
                withSalutation(expectedPerson.getSalutation()).
                withSecondName(expectedPerson.getSecondName()).
                withState(expectedPerson.getState()).
                withSurname(expectedPerson.getSurname()).
                withUserId(expectedPerson.getUserId()).
                withWorkAddress(expectedPerson.getWorkAddress()).build();

        // Then
        assertThat(actualPerson.getCreatedAt()).isEqualTo(expectedPerson.getCreatedAt());
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(expectedPerson.getDateOfBirth());
        assertThat(actualPerson.getDateOfExpiry()).isEqualTo(expectedPerson.getDateOfExpiry());
        assertThat(actualPerson.getDateOfPasswordExpiry()).isEqualTo(expectedPerson.getDateOfPasswordExpiry());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(expectedPerson.getEmailAddress());
        assertThat(actualPerson.getFirstName()).isEqualTo(expectedPerson.getFirstName());
        assertThat(actualPerson.getHomeAddress()).isEqualTo(expectedPerson.getHomeAddress());
        assertThat(actualPerson.getPassword()).isEqualTo(expectedPerson.getPassword());
        assertThat(actualPerson.getPhotographs()).isEqualTo(expectedPerson.getPhotographs());
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
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        Person expectedPerson = newPersonWithAllAssociations();

        // When
        Person actualPerson = new Person(expectedPerson.getUserId());
        actualPerson.setDateOfBirth(expectedPerson.getDateOfBirth());
        actualPerson.setDateOfExpiry(expectedPerson.getDateOfExpiry());
        actualPerson.setDateOfPasswordExpiry(expectedPerson.getDateOfPasswordExpiry());
        actualPerson.setEmailAddress(expectedPerson.getEmailAddress());
        actualPerson.setFirstName(expectedPerson.getFirstName());
        actualPerson.setHomeAddress(expectedPerson.getHomeAddress());
        actualPerson.setPassword(expectedPerson.getPassword());
        actualPerson.addPhotographs(expectedPerson.getPhotographs());
        actualPerson.setSalutation(expectedPerson.getSalutation());
        actualPerson.setSecondName(expectedPerson.getSecondName());
        actualPerson.setState(expectedPerson.getState());
        actualPerson.setSurname(expectedPerson.getSurname());
        actualPerson.setWorkAddress(expectedPerson.getWorkAddress());

        // Then
        assertThat(actualPerson.getCreatedAt()).isEqualTo(expectedPerson.getCreatedAt());
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(expectedPerson.getDateOfBirth());
        assertThat(actualPerson.getDateOfExpiry()).isEqualTo(expectedPerson.getDateOfExpiry());
        assertThat(actualPerson.getDateOfPasswordExpiry()).isEqualTo(expectedPerson.getDateOfPasswordExpiry());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(expectedPerson.getEmailAddress());
        assertThat(actualPerson.getFirstName()).isEqualTo(expectedPerson.getFirstName());
        assertThat(actualPerson.getHomeAddress()).isEqualTo(expectedPerson.getHomeAddress());
        assertThat(actualPerson.getPassword()).isEqualTo(expectedPerson.getPassword());
        assertThat(actualPerson.getPhotographs()).isEqualTo(expectedPerson.getPhotographs());
        assertThat(actualPerson.getSalutation()).isEqualTo(expectedPerson.getSalutation());
        assertThat(actualPerson.getSecondName()).isEqualTo(expectedPerson.getSecondName());
        assertThat(actualPerson.getState()).isEqualTo(expectedPerson.getState());
        assertThat(actualPerson.getSurname()).isEqualTo(expectedPerson.getSurname());
        assertThat(actualPerson.getUpdatedAt()).isEqualTo(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(expectedPerson.getUpdatedBy());
        assertThat(actualPerson.getVersion()).isEqualTo(expectedPerson.getVersion());
        assertThat(actualPerson.getWorkAddress()).isEqualTo(expectedPerson.getWorkAddress());
    }

    private Person newPersonWithAllAssociations() {
        Country country = referenceDataFactory.getCountries().get(Country.GBR);
        HomeAddress homeAddress = new HomeAddress("addressLine1", "postCode", country);
        State state = referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED);
        WorkAddress workAddress = new WorkAddress("addressLine1", "postCode", country);

        // Set the attributes
        final LocalDate dob = RandomGenerators.generateUniqueRandomDateInThePast();
        final LocalDate expiryDate = RandomGenerators.generateUniqueRandomDateInTheFuture();
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(SUFFIX_LENGTH);

        return Person.PersonBuilder.aPerson().
                withDateOfBirth(dob).
                withDateOfExpiry(expiryDate).
                withDateOfPasswordExpiry(expiryDate).
                withEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                withFirstName("Elizabeth").
                withHomeAddress(homeAddress).
                withPassword(userId).
                withSalutation("Miss").
                withSecondName("K").
                withState(state).
                withSurname("Scarlett").
                withUserId(userId).
                withWorkAddress(workAddress).build();
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
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(referenceDataFactory.getStates().get(StateCode.PERSON_DISCARDED)).build();

        // When
        person.recover();

        //Then
        assertThat(person.getState()).isEqualTo(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new Person("userId").toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_USER_ID);
    }

    @Test
    public void updatePerson() {
        // Given
        Person expectedPerson = Person.PersonBuilder.aPerson().withUserId("userId").withState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();
        Person actualPerson = new Person(expectedPerson);

        // When
        actualPerson.update();

        // Then
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Test(expected = PersonInvalidStateException.class)
    public void updatePersonWhenNotEnabled() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(referenceDataFactory.getStates().get(StateCode.PERSON_DISABLED)).build();

        // When
        person.update();

        // Then (see expected in @Test annotation)
    }

    @Test(expected = PersonInvalidStateException.class)
    public void updateRecoverWhenNotDiscarded() {
        // Given
        Person person = Person.PersonBuilder.aPerson().withUserId("userId").withState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).build();

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