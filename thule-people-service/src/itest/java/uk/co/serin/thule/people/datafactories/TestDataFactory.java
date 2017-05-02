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

        return new Person(JUNIT_TEST).
                setSalutation("Mr").setFirstName("J").setSecondName("Unit").setSurname("Tester").
                setDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast()).
                setDateOfExpiry(dateOfExpiry).
                setDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                setEmailAddress(JUNIT_TEST + EMAIL_ADDRESS_SUFFIX).
                setPassword(JUNIT_TEST).
                setState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.PERSON_ENABLED)).
                addRoles(dataFactory.getReferenceDataFactory().getRoles().values().stream());
    }

    public Person newPersonWithAllAssociations() {
        LocalDate dateOfExpiry = RandomGenerators.generateUniqueRandomDateInTheFuture();
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        Person person = new Person(userId).
                setSalutation("Miss").setFirstName("Elizabeth").setSecondName("K").setSurname("Scarlett").
                setHomeAddress(newOxfordStreetHomeAddress()).setWorkAddress(newRegentStreetWorkAddress()).
                setDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast()).
                setDateOfExpiry(RandomGenerators.generateUniqueRandomDateInTheFuture()).
                setDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                setEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                setPassword(userId).
                setState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.PERSON_ENABLED)).
                addRoles(dataFactory.getReferenceDataFactory().getRoles().values().stream());

        person.addPhotographs(Stream.of(newPhotographMissScarlett(person)));

        return person;
    }

    public Person newPersonWithoutAnyAssociations() {
        LocalDate dateOfExpiry = RandomGenerators.generateUniqueRandomDateInTheFuture();
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        return new Person(userId).
                setSalutation("Miss").setFirstName("Elizabeth").setSecondName("K").setSurname("Scarlett").
                setDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast()).
                setDateOfExpiry(RandomGenerators.generateUniqueRandomDateInTheFuture()).
                setDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                setEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                setPassword(userId);
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
}