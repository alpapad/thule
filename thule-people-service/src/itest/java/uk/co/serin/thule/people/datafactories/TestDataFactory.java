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
    public static final int USERID_SUFFIX_LENGTH = 8;
    private static final String EMAIL_ADDRESS_SUFFIX = "@serin-consultancy.co.uk";
    private static final String GREATER_LONDON = "Greater London";
    private static final String LONDON = "London";
    private final DataFactory dataFactory;

    public TestDataFactory(DataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    public Person newJUnitTest() {
        // Set the attributes
        Person person = new Person(JUNIT_TEST);
        person.setDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast());
        person.setDateOfExpiry(RandomGenerators.generateUniqueRandomDateInTheFuture());
        person.setDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), person.getDateOfExpiry()));
        person.setEmailAddress(JUNIT_TEST + EMAIL_ADDRESS_SUFFIX);
        person.setFirstName("J");
        person.setPassword(JUNIT_TEST);
        person.setSalutation("Mr");
        person.setSecondName("Unit");
        person.setSurname("Tester");

        // Set the roles
        person.addRoles(dataFactory.getReferenceDataFactory().getRoles().values().stream());

        // Set the state
        person.setState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.PERSON_ENABLED));

        return person;
    }

    private HomeAddress newOxfordStreetHomeAddress() {
        HomeAddress homeAddress = new HomeAddress("Oxford Street", "EC3", dataFactory.getReferenceDataFactory().getCountries().get(Country.GBR));
        homeAddress.setAddressLine2("Green");
        homeAddress.setCounty(GREATER_LONDON);
        homeAddress.setState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.ADDRESS_ENABLED));
        homeAddress.setTown(LONDON);
        return homeAddress;
    }

    public Person newPersonWithAllAssociations() {
        // Set the attributes
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        Person person = new Person(userId);
        person.setDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast());
        person.setDateOfExpiry(RandomGenerators.generateUniqueRandomDateInTheFuture());
        person.setDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), person.getDateOfExpiry()));
        person.setEmailAddress(userId + EMAIL_ADDRESS_SUFFIX);
        person.setFirstName("Elizabeth");
        person.setPassword(userId);
        person.setSalutation("Miss");
        person.setSecondName("K");
        person.setSurname("Scarlett");

        // Set the addresses
        person.setHomeAddress(newOxfordStreetHomeAddress());
        person.setWorkAddress(newRegentStreetWorkAddress());

        // Set the photographs
        person.addPhotographs(Stream.of(newPhotographMissScarlett(person)));

        // Set the roles
        person.addRoles(dataFactory.getReferenceDataFactory().getRoles().values().stream());

        // Set the state
        person.setState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.PERSON_ENABLED));

        return person;
    }

    public Person newPersonWithoutAnyAssociations() {
        // Set the attributes
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        Person person = new Person(userId);
        person.setDateOfBirth(RandomGenerators.generateUniqueRandomDateInThePast());
        person.setDateOfExpiry(RandomGenerators.generateUniqueRandomDateInTheFuture());
        person.setDateOfPasswordExpiry(RandomGenerators.generateUniqueRandomDateBetween(LocalDate.now(), person.getDateOfExpiry()));
        person.setEmailAddress(userId + EMAIL_ADDRESS_SUFFIX);
        person.setFirstName("Elizabeth");
        person.setPassword(userId);
        person.setSalutation("Miss");
        person.setSecondName("K");
        person.setSurname("Scarlett");

        return person;
    }

    private Photograph newPhotographMissScarlett(Person person) {
        try {
            Resource resource = new DefaultResourceLoader().getResource("db/photographs/missScarlet.jpg");
            Photograph photograph = new Photograph(FileCopyUtils.copyToByteArray(resource.getInputStream()), person);
            photograph.setPosition(1);

            return photograph;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private WorkAddress newRegentStreetWorkAddress() {
        WorkAddress workAddress = new WorkAddress("Regent Street", "EC4", dataFactory.getReferenceDataFactory().getCountries().get(Country.GBR));
        workAddress.setAddressLine2("Green");
        workAddress.setCounty(GREATER_LONDON);
        workAddress.setState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.ADDRESS_ENABLED));
        workAddress.setTown(LONDON);
        return workAddress;
    }
}