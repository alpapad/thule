package uk.co.serin.thule.people.datafactory;

import uk.co.serin.thule.utils.utils.RandomUtils;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import uk.co.serin.thule.people.domain.address.HomeAddress;
import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.email.Email;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataFactory implements ReferenceDataFactory {
    public static final String JUNIT_TEST_USERNAME = "user";
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

    public Email buildEmail() {
        return Email.EmailBuilder.anEmail().
                withBody("This is a test body").
                withSubject("Test subject").
                withTos(Stream.of("to1@test.co.uk", "to2@test.co.uk", "to3@test.co.uk").collect(Collectors.toSet())).
                build();
    }

    public Person buildPerson(Person person) {
        return Person.PersonBuilder.aPerson().
                withCreatedAt(person.getCreatedAt()).
                withCreatedBy(person.getCreatedBy()).
                withDateOfBirth(person.getDateOfBirth()).
                withDateOfExpiry(person.getDateOfExpiry()).
                withDateOfPasswordExpiry(person.getDateOfPasswordExpiry()).
                withEmailAddress(person.getEmailAddress()).
                withFirstName(person.getFirstName()).
                withHomeAddress(person.getHomeAddress()).
                withId(person.getId()).
                withLastName(person.getLastName()).
                withPassword(person.getPassword()).
                withPhotographs(person.getPhotographs()).
                withRoles(person.getRoles()).
                withSecondName(person.getSecondName()).
                withState(person.getState()).
                withTitle(person.getTitle()).
                withUpdatedAt(person.getUpdatedAt()).
                withUpdatedBy(person.getUpdatedBy()).
                withUserId(person.getUserId()).
                withVersion(person.getVersion()).
                withWorkAddress(person.getWorkAddress()).build();
    }

    public Person buildPersonWithAllAssociations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate dateOfExpiry = RandomUtils.generateUniqueRandomDateAfter(LocalDate.now().plus(1, ChronoUnit.DAYS));
        String userId = "missScarlett" + RandomUtils.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        Person person = Person.PersonBuilder.aPerson().withUserId(userId).
                withCreatedAt(now).
                withCreatedBy(JUNIT_TEST_USERNAME).
                withDateOfBirth(RandomUtils.generateUniqueRandomDateInThePast()).
                withDateOfExpiry(RandomUtils.generateUniqueRandomDateInTheFuture()).
                withDateOfPasswordExpiry(RandomUtils.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                withEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                withFirstName("Elizabeth").
                withHomeAddress(buildOxfordStreetHomeAddress()).
                withLastName("Scarlett").
                withPassword(userId).
                withRoles(new HashSet<>(referenceDataFactory.getRoles().values())).
                withSecondName("K").
                withState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED)).
                withTitle("Miss").
                withWorkAddress(buildRegentStreetWorkAddress()).
                withUpdatedAt(now).
                withUpdatedBy(JUNIT_TEST_USERNAME).
                build();

        person.addPhotographs(Stream.of(buildPhotographMissScarlett(person)).collect(Collectors.toSet()));

        return person;
    }

    public HomeAddress buildOxfordStreetHomeAddress() {
        LocalDateTime now = LocalDateTime.now();
        return HomeAddress.HomeAddressBuilder.aHomeAddress().
                withCreatedAt(now).
                withCreatedBy(JUNIT_TEST_USERNAME).
                withAddressLine1("Oxford Street").
                withAddressLine2("Green").
                withCountry(referenceDataFactory.getCountries().get(Country.GBR)).
                withCounty(GREATER_LONDON).
                withPostCode("EC3").
                withState(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).
                withTown(LONDON).
                withUpdatedAt(now).
                withUpdatedBy(JUNIT_TEST_USERNAME).
                build();
    }

    public WorkAddress buildRegentStreetWorkAddress() {
        LocalDateTime now = LocalDateTime.now();
        return WorkAddress.WorkAddressBuilder.aWorkAddress().
                withCreatedAt(now).
                withCreatedBy(JUNIT_TEST_USERNAME).
                withAddressLine1("Regent Street").
                withAddressLine2("Green").
                withCountry(referenceDataFactory.getCountries().get(Country.GBR)).
                withCounty(GREATER_LONDON).
                withPostCode("EC4").
                withState(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).
                withTown(LONDON).
                withUpdatedAt(now).
                withUpdatedBy(JUNIT_TEST_USERNAME).
                build();
    }

    public Photograph buildPhotographMissScarlett(Person person) {
        try {
            LocalDateTime now = LocalDateTime.now();
            Resource resource = new DefaultResourceLoader().getResource("photographs/missScarlet.jpg");
            return Photograph.PhotographBuilder.aPhotograph().
                    withCreatedAt(now).
                    withCreatedBy(JUNIT_TEST_USERNAME).
                    withPerson(person).
                    withPhoto(FileCopyUtils.copyToByteArray(resource.getInputStream())).
                    withPosition(1).
                    withUpdatedAt(now).
                    withUpdatedBy(JUNIT_TEST_USERNAME).
                    build();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Person buildPersonWithoutAnyAssociations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate dateOfExpiry = RandomUtils.generateUniqueRandomDateAfter(LocalDate.now().plus(1, ChronoUnit.DAYS));
        String userId = "missScarlett" + RandomUtils.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        return Person.PersonBuilder.aPerson().
                withCreatedAt(now).
                withCreatedBy(JUNIT_TEST_USERNAME).
                withDateOfBirth(RandomUtils.generateUniqueRandomDateInThePast()).
                withDateOfExpiry(RandomUtils.generateUniqueRandomDateInTheFuture()).
                withDateOfPasswordExpiry(RandomUtils.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                withEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                withFirstName("Elizabeth").
                withLastName("Scarlett").
                withPassword(userId).
                withSecondName("K").
                withTitle("Miss").
                withUserId(userId).
                withUpdatedAt(now).
                withUpdatedBy(JUNIT_TEST_USERNAME).
                build();
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
}