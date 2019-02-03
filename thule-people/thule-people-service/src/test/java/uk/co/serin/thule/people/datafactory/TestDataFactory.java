package uk.co.serin.thule.people.datafactory;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.DigestUtils;
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
import uk.co.serin.thule.utils.utils.RandomUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
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
        return Email.builder().
                body("This is a test body").
                            subject("Test subject").
                            tos(Stream.of("to1@test.co.uk", "to2@test.co.uk", "to3@test.co.uk").collect(Collectors.toSet())).
                            build();
    }

    public Person buildPerson(Person person) {
        return Person.builder().
                dateOfBirth(person.getDateOfBirth()).
                             dateOfExpiry(person.getDateOfExpiry()).
                             dateOfPasswordExpiry(person.getDateOfPasswordExpiry()).
                             emailAddress(person.getEmailAddress()).
                             firstName(person.getFirstName()).
                             homeAddress(person.getHomeAddress()).
                             lastName(person.getLastName()).
                             password(person.getPassword()).
                             photographs(person.getPhotographs()).
                             roles(person.getRoles()).
                             secondName(person.getSecondName()).
                             state(person.getState()).
                             title(person.getTitle()).
                             userId(person.getUserId()).
                             workAddress(person.getWorkAddress()).build();
    }

    public Person buildPersonWithAllAssociations() {
        Person person = buildPersonWithoutAnyAssociations();
        person.setHomeAddress(buildOxfordStreetHomeAddress());
        person.setPhotographs(Stream.of(buildPhotographMissScarlett(person)).collect(Collectors.toSet()));
        person.setRoles(new HashSet<>(referenceDataFactory.getRoles().values()));
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));
        person.setWorkAddress(buildRegentStreetWorkAddress());

        return person;
    }

    public Person buildPersonWithoutAnyAssociations() {
        LocalDate dateOfExpiry = RandomUtils.generateUniqueRandomDateAfter(LocalDate.now().plus(1, ChronoUnit.DAYS));
        String userId = "missScarlett" + RandomUtils.generateUniqueRandomString(USERID_SUFFIX_LENGTH);

        return Person.builder().
                dateOfBirth(RandomUtils.generateUniqueRandomDateInThePast()).
                             dateOfExpiry(RandomUtils.generateUniqueRandomDateInTheFuture()).
                             dateOfPasswordExpiry(RandomUtils.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                             emailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                             firstName("Elizabeth").
                             lastName("Scarlett").
                             password(userId).
                             secondName("K").
                             title("Miss").
                             userId(userId).
                             build();
    }

    public HomeAddress buildOxfordStreetHomeAddress() {
        return HomeAddress.builder().
                addressLine1("Oxford Street").
                                  addressLine2("Green").
                                  country(referenceDataFactory.getCountries().get(Country.GBR)).
                                  county(GREATER_LONDON).
                                  postCode("EC3").
                                  state(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).
                                  town(LONDON).
                                  build();
    }

    public Photograph buildPhotographMissScarlett(Person person) {
        try {
            Resource resource = new DefaultResourceLoader().getResource("photographs/missScarlet.jpg");
            byte[] photo = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return Photograph.builder().
                    hash(new String(DigestUtils.md5Digest(photo), Charset.defaultCharset())).
                                     person(person).
                                     photo(photo).
                                     position(1).
                                     build();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public WorkAddress buildRegentStreetWorkAddress() {
        return WorkAddress.builder().
                addressLine1("Regent Street").
                                  addressLine2("Green").
                                  country(referenceDataFactory.getCountries().get(Country.GBR)).
                                  county(GREATER_LONDON).
                                  postCode("EC4").
                                  state(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).
                                  town(LONDON).
                                  build();
    }

    public Person buildPersonWithState() {
        Person person = buildPersonWithoutAnyAssociations();
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));
        return person;
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