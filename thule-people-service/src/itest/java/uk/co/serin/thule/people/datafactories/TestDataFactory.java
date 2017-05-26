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
import uk.co.serin.thule.people.domain.state.StateCode;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataFactory {
    public static final String JUNIT_TEST = "JUnitTest";
    private static final String EMAIL_ADDRESS_SUFFIX = "@serin-consultancy.co.uk";
    private static final String GREATER_LONDON = "Greater London";
    private static final String LONDON = "London";
    private static final int USERID_SUFFIX_LENGTH = 8;
    private final DataFactory dataFactory;

    TestDataFactory(DataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    public Person newJUnitTest() {
        LocalDate dateOfExpiry = RandomGenerators.generateUniqueRandomDateInTheFuture();

        return Person.PersonBuilder.aPerson().withUserId(JUNIT_TEST).
                withSalutation("Mr").withFirstName("J").withSecondName("Unit").withSurname("Tester").
                withDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast()).
                withDateOfExpiry(dateOfExpiry).
                withDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                withEmailAddress(JUNIT_TEST + EMAIL_ADDRESS_SUFFIX).
                withPassword(JUNIT_TEST).
                withState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.PERSON_ENABLED)).
                withRoles(new HashSet<>(dataFactory.getReferenceDataFactory().getRoles().values())).build();
    }

    public Person newPersonWithAllAssociations() {
        LocalDate dateOfExpiry = RandomGenerators.generateUniqueRandomDateInTheFuture();
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        Person person = Person.PersonBuilder.aPerson().withUserId(userId).
                withSalutation("Miss").withFirstName("Elizabeth").withSecondName("K").withSurname("Scarlett").
                withHomeAddress(newOxfordStreetHomeAddress()).withWorkAddress(newRegentStreetWorkAddress()).
                withDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast()).
                withDateOfExpiry(RandomGenerators.generateUniqueRandomDateInTheFuture()).
                withDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                withEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                withPassword(userId).
                withState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.PERSON_ENABLED)).
                withRoles(new HashSet<>(dataFactory.getReferenceDataFactory().getRoles().values())).build();

        person.addPhotographs(Stream.of(newPhotographMissScarlett(person)).collect(Collectors.toSet()));

        return person;
    }

    private HomeAddress newOxfordStreetHomeAddress() {
        return new HomeAddress("Oxford Street", "EC3", dataFactory.getReferenceDataFactory().getCountries().get(Country.GBR)).
                setAddressLine2("Green").setCounty(GREATER_LONDON).setState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.ADDRESS_ENABLED)).setTown(LONDON);
    }

    private WorkAddress newRegentStreetWorkAddress() {
        return new WorkAddress("Regent Street", "EC4", dataFactory.getReferenceDataFactory().getCountries().get(Country.GBR)).
                setAddressLine2("Green").setCounty(GREATER_LONDON).setState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.ADDRESS_ENABLED)).setTown(LONDON);
    }

    private Photograph newPhotographMissScarlett(Person person) {
        try {
            Resource resource = new DefaultResourceLoader().getResource("db/photographs/missScarlet.jpg");
            return new Photograph(FileCopyUtils.copyToByteArray(resource.getInputStream()), person).setPosition(1);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Person newPersonWithoutAnyAssociations() {
        LocalDate dateOfExpiry = RandomGenerators.generateUniqueRandomDateInTheFuture();
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        return Person.PersonBuilder.aPerson().withUserId(userId).
                withSalutation("Miss").withFirstName("Elizabeth").withSecondName("K").withSurname("Scarlett").
                withDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast()).
                withDateOfExpiry(RandomGenerators.generateUniqueRandomDateInTheFuture()).
                withDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                withEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                withPassword(userId).build();
    }
}