package uk.co.serin.thule.people;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;

import uk.co.serin.thule.people.datafactory.ReferenceDataFactory;
import uk.co.serin.thule.people.domain.entity.address.HomeAddressEntity;
import uk.co.serin.thule.people.domain.entity.address.WorkAddressEntity;
import uk.co.serin.thule.people.domain.entity.country.CountryEntity;
import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.entity.person.PhotographEntity;
import uk.co.serin.thule.people.domain.model.state.StateCode;
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

    public PersonEntity buildPersonWithAllAssociations() {
        var dateOfExpiry = RandomUtils.generateUniqueRandomDateAfter(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var userId = "missScarlett" + RandomUtils.generateUniqueRandomString(8);

        var person = PersonEntity.builder().
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

    private HomeAddressEntity buildOxfordStreetHomeAddress() {
        return HomeAddressEntity.builder().
                addressLine1("Oxford Street").
                                  addressLine2("Green").
                                  country(referenceDataFactory.getCountries().get(CountryEntity.GBR)).
                                  county(GREATER_LONDON).
                                  postCode("EC3").
                                  state(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).
                                  town(LONDON).
                                  build();
    }

    private PhotographEntity buildPhotographMissScarlett(PersonEntity personEntity) {
        try {
            var resource = new DefaultResourceLoader().getResource("photographs/missScarlet.jpg");
            var photo = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return PhotographEntity.builder().
                    hash(new String(DigestUtils.md5Digest(photo), Charset.defaultCharset())).
                                           person(personEntity).
                                     photo(photo).
                                     position(1).
                                     build();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private WorkAddressEntity buildRegentStreetWorkAddress() {
        return WorkAddressEntity.builder().
                addressLine1("Regent Street").
                                  addressLine2("Green").
                                  country(referenceDataFactory.getCountries().get(CountryEntity.GBR)).
                                  county(GREATER_LONDON).
                                  postCode("EC4").
                                  state(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).
                                  town(LONDON).
                                  build();
    }
}