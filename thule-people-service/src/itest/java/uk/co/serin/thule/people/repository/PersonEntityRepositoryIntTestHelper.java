package uk.co.serin.thule.people.repository;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.FileCopyUtils;

import uk.co.serin.thule.people.domain.entity.account.AccountEntity;
import uk.co.serin.thule.people.domain.entity.address.HomeAddressEntity;
import uk.co.serin.thule.people.domain.entity.address.WorkAddressEntity;
import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleEntity;
import uk.co.serin.thule.people.domain.model.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.CountryRepository;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;
import uk.co.serin.thule.utils.utils.RandomUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PersonEntityRepositoryIntTestHelper {
    private final CountryRepository countryRepository;
    private final RoleRepository roleRepository;
    private final StateRepository stateRepository;

    PersonEntityRepositoryIntTestHelper(CountryRepository countryRepository, RoleRepository roleRepository, StateRepository stateRepository) {
        this.countryRepository = countryRepository;
        this.roleRepository = roleRepository;
        this.stateRepository = stateRepository;
    }

    PersonEntity buildPersonWithAllAssociations() {
        var dateOfExpiry = RandomUtils.generateUniqueRandomDateAfter(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var userId = "missScarlett" + RandomUtils.generateUniqueRandomString(8);

        byte[] photograph;
        try {
            var resource = new DefaultResourceLoader().getResource("photographs/missScarlet.jpg");
            photograph = FileCopyUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        var person = PersonEntity.builder().dateOfBirth(RandomUtils.generateUniqueRandomDateInThePast())
                                 .dateOfExpiry(RandomUtils.generateUniqueRandomDateInTheFuture())
                                 .dateOfPasswordExpiry(RandomUtils.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry))
                                 .emailAddress(userId + "@serin-consultancy.co.uk").firstName("Elizabeth").lastName("Scarlett").password(userId)
                                 .photograph(photograph).secondName("K").title("Miss").userId(userId).build();

        var roles = new HashSet<RoleEntity>();
        roleRepository.findAll().forEach(roles::add);

        person.setAccounts(Stream.of(buildAccount12345678(person)).collect(Collectors.toSet()));
        person.setHomeAddress(buildOxfordStreetHomeAddress());
        person.setRoles(roles);
        person.setState(stateRepository.findByCode(StateCode.PERSON_ENABLED));
        person.setWorkAddress(buildRegentStreetWorkAddress());

        return person;
    }

    private AccountEntity buildAccount12345678(PersonEntity personEntity) {
        return AccountEntity.builder().number(12345678).person(personEntity).build();
    }

    private HomeAddressEntity buildOxfordStreetHomeAddress() {
        return HomeAddressEntity.builder().addressLine1("Oxford Street").addressLine2("Green")
                                .country(countryRepository.findByIsoCodeThreeCharacters("GBR")).county("Greater London").postCode("EC3")
                                .state(stateRepository.findByCode(StateCode.ADDRESS_ENABLED)).town("London").build();
    }

    private WorkAddressEntity buildRegentStreetWorkAddress() {
        return WorkAddressEntity.builder().addressLine1("Regent Street").addressLine2("Green")
                                .country(countryRepository.findByIsoCodeThreeCharacters("GBR")).county("Greater London").postCode("EC4")
                                .state(stateRepository.findByCode(StateCode.ADDRESS_ENABLED)).town("London").build();
    }
}