package uk.co.serin.thule.people.datafactories;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import uk.co.serin.thule.core.utils.RandomGenerators;
import uk.co.serin.thule.people.domain.address.HomeAddress;
import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.person.Photograph;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataFactory implements ReferenceDataFactory {
    public static final String JUNIT_TEST = "JUnitTest";
    private static final String EMAIL_ADDRESS_SUFFIX = "@serin-consultancy.co.uk";
    private static final String GREATER_LONDON = "Greater London";
    private static final String LONDON = "London";
    private static final int USERID_SUFFIX_LENGTH = 8;
    private final ReferenceDataFactory referenceDataFactory;

    public TestDataFactory(ReferenceDataFactory referenceDataFactory) {
        this.referenceDataFactory = referenceDataFactory;
    }

    public TestDataFactory() {
        this.referenceDataFactory = new MockReferenceDataFactory();
    }

    @Override
    public Map<ActionCode, Action> getActions() {
        return referenceDataFactory.getActions();
    }

    @Override
    public Map<String, Country> getCountries() {
        return referenceDataFactory.getCountries();
    }

    @Override
    public Map<RoleCode, Role> getRoles() {
        return referenceDataFactory.getRoles();
    }

    @Override
    public Map<StateCode, State> getStates() {
        return referenceDataFactory.getStates();
    }

    public Person newJUnitTest() {
        LocalDate dateOfExpiry = RandomGenerators.generateUniqueRandomDateInTheFuture();

        return Person.PersonBuilder.aPerson().withUserId(JUNIT_TEST).
                withDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast()).
                withDateOfExpiry(dateOfExpiry).
                withDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                withEmailAddress(JUNIT_TEST + EMAIL_ADDRESS_SUFFIX).
                withFirstName("J").
                withPassword(JUNIT_TEST).
                withRoles(new HashSet<>(referenceDataFactory.getRoles().values())).
                withSalutation("Mr").
                withSecondName("Unit").
                withState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).
                withSurname("Tester").
                build();
    }

    public Person newPerson(Person person) {
        return Person.PersonBuilder.aPerson().
                withCreatedAt(person.getCreatedAt()).
                withDateOfBirth(person.getDateOfBirth()).
                withDateOfExpiry(person.getDateOfExpiry()).
                withDateOfPasswordExpiry(person.getDateOfPasswordExpiry()).
                withEmailAddress(person.getEmailAddress()).
                withFirstName(person.getFirstName()).
                withHomeAddress(person.getHomeAddress()).
                withId(person.getId()).
                withPassword(person.getPassword()).
                withPhotographs(person.getPhotographs()).
                withRoles(person.getRoles()).
                withSalutation(person.getSalutation()).
                withSecondName(person.getSecondName()).
                withState(person.getState()).
                withSurname(person.getSurname()).
                withUpdatedAt(person.getUpdatedAt()).
                withUpdatedBy(person.getUpdatedBy()).
                withUserId(person.getUserId()).
                withVersion(person.getVersion()).
                withWorkAddress(person.getWorkAddress()).build();
    }

    public Person newPersonWithAllAssociations() {
        LocalDate dateOfExpiry = RandomGenerators.generateUniqueRandomDateInTheFuture();
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        Person person = Person.PersonBuilder.aPerson().withUserId(userId).
                withDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast()).
                withDateOfExpiry(RandomGenerators.generateUniqueRandomDateInTheFuture()).
                withDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                withEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                withFirstName("Elizabeth").
                withHomeAddress(newOxfordStreetHomeAddress()).
                withPassword(userId).
                withRoles(new HashSet<>(referenceDataFactory.getRoles().values())).
                withSalutation("Miss").
                withSecondName("K").
                withState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).
                withSurname("Scarlett").
                withWorkAddress(newRegentStreetWorkAddress()).
                build();

        person.addPhotographs(Stream.of(newPhotographMissScarlett(person)).collect(Collectors.toSet()));

        return person;
    }

    public HomeAddress newOxfordStreetHomeAddress() {
        return HomeAddress.HomeAddressBuilder.aHomeAddress().
                withAddressLine1("Oxford Street").
                withAddressLine2("Green").
                withCountry(referenceDataFactory.getCountries().get(Country.GBR)).
                withCounty(GREATER_LONDON).
                withPostCode("EC3").
                withState(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).
                withTown(LONDON).
                build();
    }

    public WorkAddress newRegentStreetWorkAddress() {
        return WorkAddress.WorkAddressBuilder.aWorkAddress().
                withAddressLine1("Regent Street").
                withAddressLine2("Green").
                withCountry(referenceDataFactory.getCountries().get(Country.GBR)).
                withCounty(GREATER_LONDON).
                withPostCode("EC4").
                withState(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).
                withTown(LONDON).
                build();
    }

    public Photograph newPhotographMissScarlett(Person person) {
        try {
            Resource resource = new DefaultResourceLoader().getResource("db/photographs/missScarlet.jpg");
            return Photograph.PhotographBuilder.aPhotograph().
                    withPerson(person).
                    withPhoto(FileCopyUtils.copyToByteArray(resource.getInputStream())).
                    withPosition(1).
                    build();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Person newPersonWithoutAnyAssociations() {
        LocalDate dateOfExpiry = RandomGenerators.generateUniqueRandomDateInTheFuture();
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        return Person.PersonBuilder.aPerson().
                withDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast()).
                withDateOfExpiry(RandomGenerators.generateUniqueRandomDateInTheFuture()).
                withDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                withEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                withFirstName("Elizabeth").
                withPassword(userId).
                withSalutation("Miss").
                withSecondName("K").
                withSurname("Scarlett").
                withUserId(userId).
                build();
    }
}