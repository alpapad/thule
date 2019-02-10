package uk.co.serin.thule.people;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;

import uk.co.serin.thule.people.datafactory.ReferenceDataFactory;
import uk.co.serin.thule.people.domain.address.HomeAddress;
import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.person.Photograph;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.utils.utils.RandomUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestPersonDataFactory {
    private static final String GREATER_LONDON = "Greater London";
    private static final String LONDON = "London";
    private ReferenceDataFactory referenceDataFactory;

    public TestPersonDataFactory(ReferenceDataFactory referenceDataFactory) {
        this.referenceDataFactory = referenceDataFactory;
    }

    public Person buildPersonWithAllAssociations() {
        var dateOfExpiry = RandomUtils.generateUniqueRandomDateAfter(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var userId = "missScarlett" + RandomUtils.generateUniqueRandomString(8);

        var person = Person.builder().
                dateOfBirth(RandomUtils.generateUniqueRandomDateInThePast()).
                                   dateOfExpiry(RandomUtils.generateUniqueRandomDateInTheFuture()).
                                   dateOfPasswordExpiry(RandomUtils.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                                   emailAddress(userId + "@serin-consultancy.co.uk").
                                   firstName("Elizabeth").
                                   lastName("Scarlett").
                                   password(userId).
                                   secondName("K").
                                   title("Miss").
                                   userId(userId).
                                   build();

        person.setHomeAddress(buildOxfordStreetHomeAddress());
        person.setPhotographs(Stream.of(buildPhotographMissScarlett(person)).collect(Collectors.toSet()));
        person.setRoles(new HashSet<>(referenceDataFactory.getRoles().values()));
        person.setState(referenceDataFactory.getStates().get(StateCode.PERSON_ENABLED));
        person.setWorkAddress(buildRegentStreetWorkAddress());

        return person;
    }

    private HomeAddress buildOxfordStreetHomeAddress() {
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

    private Photograph buildPhotographMissScarlett(Person person) {
        try {
            var resource = new DefaultResourceLoader().getResource("photographs/missScarlet.jpg");
            var photo = FileCopyUtils.copyToByteArray(resource.getInputStream());
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

    private WorkAddress buildRegentStreetWorkAddress() {
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
}