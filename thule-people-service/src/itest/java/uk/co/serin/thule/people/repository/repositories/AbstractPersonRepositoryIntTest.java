package uk.co.serin.thule.people.repository.repositories;

import org.hibernate.LazyInitializationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import uk.co.serin.thule.people.datafactories.ReferenceDataFactory;
import uk.co.serin.thule.people.datafactories.RepositoryReferenceDataFactory;
import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.address.HomeAddress;
import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.person.Photograph;
import uk.co.serin.thule.people.domain.role.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"spring.config.location=classpath:/config/${spring.application.name}/"})
@RunWith(SpringRunner.class)
public abstract class AbstractPersonRepositoryIntTest {
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StateRepository stateRepository;
    private TestDataFactory testDataFactory;

    @Test(expected = LazyInitializationException.class)
    public void accessLazyAssociatedPhotographsOutsideTransaction() {
        // Given
        Person person = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        person = personRepository.findOne(person.getId());

        // When
        new HashSet<>(person.getPhotographs());

        // Then (see expected in @Test annotation)
    }

    @Test(expected = LazyInitializationException.class)
    public void accessLazyAssociatedRolesOutsideTransaction() {
        // Given
        Person person = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        person = personRepository.findOne(person.getId());

        // When
        new HashSet<>(person.getPhotographs());

        // Then (see expected in @Test annotation)
    }

    @Test
    public void accessPreFetchedAssociationsOutsideTransaction() {
        // Given
        Person person = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        person = personRepository.findByIdAndFetchAllAssociations(person.getId());

        // When
        Set<Photograph> photographs = person.getPhotographs();
        Set<Role> roles = person.getRoles();

        // Then
        assertThat(new HashSet<>(photographs)).isNotEmpty();
        assertThat(new HashSet<>(roles)).isNotEmpty();
    }

    @Test
    @Transactional
    public void create() {
        // Given
        Person testPerson = testDataFactory.newPersonWithAllAssociations();
        Person expectedPerson = testDataFactory.newPerson(testPerson);
        ReflectionTestUtils.setField(expectedPerson, DomainModel.ENTITY_ATTRIBUTE_NAME_VERSION, 0L);

        // When
        personRepository.save(testPerson);

        // Then
        Person actualPerson = personRepository.findByUserIdAndFetchAllAssociations(testPerson.getUserId());

        assertThat(actualPerson.getId()).isNotNull();
        assertThat(actualPerson.getUpdatedAt()).isNotNull();
        assertThat(actualPerson.getCreatedAt()).isEqualTo(actualPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isNotNull();

        assertThat(actualPerson.getPhotographs()).hasSize(1);
        Photograph actualPhotograph = actualPerson.getPhotographs().iterator().next();
        assertThat(actualPhotograph.getId()).isNotNull();
        assertThat(actualPhotograph.getVersion()).isEqualTo(0);

        HomeAddress actualHomeAddress = actualPerson.getHomeAddress();
        assertThat(actualHomeAddress.getId()).isNotNull();
        assertThat(actualHomeAddress.getVersion()).isEqualTo(0);

        WorkAddress actualWorkAddress = actualPerson.getWorkAddress();
        assertThat(actualWorkAddress.getId()).isNotNull();
        assertThat(actualWorkAddress.getVersion()).isEqualTo(0);

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson,
                DomainModel.ENTITY_ATTRIBUTE_NAME_ID,
                DomainModel.ENTITY_ATTRIBUTE_NAME_CREATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);
    }

    @Test
    public void createAPersonViolatingValidationConstraints() {
        // Given
        Person person = new Person("userId");

        // When
        Throwable cause = null;
        try {
            personRepository.save(person);
            fail();
        } catch (TransactionSystemException e) { // Hsql and Oracle
            cause = e.getMostSpecificCause();
        } catch (ConstraintViolationException e) { // MySql
            cause = e;
        }

        // Then
        assertThat(cause).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @Transactional
    public void delete() {
        // Given
        Person person = personRepository.save(testDataFactory.newPersonWithAllAssociations());

        // When
        personRepository.delete(person);

        // Then
        person = personRepository.findOne(person.getId());
        assertThat(person).isNull();
    }

    @Test
    @Transactional
    public void findAll() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.newPerson(testPerson);

        // When
        List<Person> actualPeople = personRepository.findAll();

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    @Transactional
    public void findByCriteria() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.newPerson(testPerson);

        // When
        List<Person> actualPeople = personRepository.findByCriteria(testPerson.getEmailAddress().substring(1), null, null, null);

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    @Transactional
    public void findById() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.newPerson(testPerson);

        // When
        Person actualPerson = personRepository.findOne(testPerson.getId());

        // Then
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Test
    @Transactional
    public void findByIdAndFetchAllAssociations() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.newPerson(testPerson);

        // When
        Person actualPerson = personRepository.findByIdAndFetchAllAssociations(testPerson.getId());

        // Then
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Test
    @Transactional
    public void findByUpdatedBy() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.newPerson(testPerson);

        // When
        Set<Person> actualPeople = personRepository.findByUpdatedBy(TestDataFactory.JUNIT_TEST);

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    @Transactional
    public void findByUserid() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.newPerson(testPerson);

        // When
        Person actualPerson = personRepository.findByUserIdAndFetchAllAssociations(testPerson.getUserId());

        // Then
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Test
    @Transactional
    public void searchPeople() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.newPerson(testPerson);

        // When
        List<Person> actualPeople = personRepository.search(testPerson.getEmailAddress().substring(1));

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Before
    public void setUp() {
        ReferenceDataFactory referenceDataFactory = new RepositoryReferenceDataFactory(actionRepository, stateRepository, roleRepository, countryRepository);
        testDataFactory = new TestDataFactory(referenceDataFactory);

        // Delete previous test data
        Set<Person> people = personRepository.findByUpdatedBy(TestDataFactory.JUNIT_TEST);
        personRepository.delete(people);

        // Setup security context
        Person jUnitTestPerson = testDataFactory.newJUnitTest();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(jUnitTestPerson.getUserId(), jUnitTestPerson.getPassword()));
    }

    @Test
    @Transactional
    public void update() throws InterruptedException {
        // Given
        Person testPerson = personRepository.save(testDataFactory.newPersonWithAllAssociations());
        testPerson.setFirstName("updatedFirstName");
        testPerson.setSecondName("updatedSecondName");
        testPerson.setSurname("updatedSurname");
        testPerson.setDateOfBirth(testPerson.getDateOfBirth().minusDays(1));
        testPerson.setEmailAddress("updated@gmail.com");
        testPerson.setPassword("updatedPassword");

        Person expectedPerson = testDataFactory.newPerson(testPerson);
        ReflectionTestUtils.setField(expectedPerson, DomainModel.ENTITY_ATTRIBUTE_NAME_VERSION, testPerson.getVersion() + 1);

        Thread.sleep(10L); // Allow enough time to lapse for the updatedAt to be updated with a different value

        // When
        personRepository.save(testPerson);

        // Then
        Person actualPerson = personRepository.findByUserIdAndFetchAllAssociations(testPerson.getUserId());

        assertThat(actualPerson.getUpdatedAt()).isAfter(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isNotEmpty();

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);
    }
}
