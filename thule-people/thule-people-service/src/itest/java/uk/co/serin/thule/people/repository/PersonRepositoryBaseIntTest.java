package uk.co.serin.thule.people.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import uk.co.serin.thule.people.datafactory.RepositoryReferenceDataFactory;
import uk.co.serin.thule.people.datafactory.TestDataFactory;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.repository.repositories.ActionRepository;
import uk.co.serin.thule.people.repository.repositories.CountryRepository;
import uk.co.serin.thule.people.repository.repositories.PersonRepository;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.fail;

/**
 * Abstract class for testing the repository in both MySql and H2
 *
 * This test uses @DataJpaTest to test boot just the required parts of Spring Boot for JPA
 * repository testing. It does this by disabling auto configuration (including the use of
 *
 * @Configuration) and using select auto configuration classes. However, this results in JPA
 * Auditing not being configured because it is enabled in a @Configuration class! Therefore
 * it is enabled in the inner configuration class.
 */
@DataJpaTest
@Import(RepositoryIntTestConfiguration.class)
@RunWith(SpringRunner.class)
@WithMockUser(username = TestDataFactory.JUNIT_TEST_USERNAME, password = TestDataFactory.JUNIT_TEST_USERNAME)
public abstract class PersonRepositoryBaseIntTest {
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
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void given_a_new_person_when_finding_all_people_then_the_new_person_is_found() {
        // Given
        var expectedPerson = createAndPersistPerson();

        // When
        var actualPeople = personRepository.findAll();

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    private Person createAndPersistPerson() {
        var expectedPerson = testDataFactory.buildPersonWithAllAssociations();

        var person = personRepository.saveAndFlush(expectedPerson);
        testEntityManager.clear();

        return person;
    }

    @Test
    public void given_a_new_person_when_finding_that_person_by_id_then_the_new_person_is_found() {
        // Given
        var expectedPerson = createAndPersistPerson();

        // When
        var actualOptionalPerson = personRepository.findById(expectedPerson.getId());

        // Then
        var actualPerson = actualOptionalPerson.orElseThrow();
        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_finding_that_person_by_id_then_the_new_person_is_found_with_all_associations() {
        // Given
        var expectedPerson = createAndPersistPerson();

        // When
        var actualPerson = personRepository.findByIdAndFetchAllAssociations(expectedPerson.getId());

        // Then
        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_finding_that_person_by_updatedBy_then_the_new_person_is_found() {
        // Given
        var expectedPerson = createAndPersistPerson();

        // When
        var actualPeople = personRepository.findByUpdatedBy(TestDataFactory.JUNIT_TEST_USERNAME);

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_finding_that_person_by_userid_then_the_new_person_is_found() {
        // Given
        var expectedPerson = createAndPersistPerson();

        // When
        var actualPerson = personRepository.findByUserIdAndFetchAllAssociations(expectedPerson.getUserId());

        // Then
        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_finding_that_person_with_criteria_then_the_new_person_is_found() {
        // Given
        var expectedPerson = createAndPersistPerson();

        // When
        var actualPeople = personRepository.findByCriteria(expectedPerson.getEmailAddress().substring(1), null, null, null);

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_searching_by_email_address_then_the_new_person_is_found() {
        // Given
        var expectedPerson = createAndPersistPerson();

        // When
        var actualPeople = personRepository.search(expectedPerson.getEmailAddress().substring(1));

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_updating_that_person_then_the_new_person_is_found_with_updated_fields() throws InterruptedException {
        // Given
        var expectedPerson = createAndPersistPerson();

        expectedPerson.setFirstName("updatedFirstName");
        expectedPerson.setSecondName("updatedSecondName");
        expectedPerson.setLastName("updatedLastName");
        expectedPerson.setDateOfBirth(expectedPerson.getDateOfBirth().minusDays(1));
        expectedPerson.setEmailAddress("updated@gmail.com");
        expectedPerson.setPassword("updatedPassword");

        Thread.sleep(10L); // Allow enough time to lapse for the updatedAt to be updated with a different value

        // When
        personRepository.save(expectedPerson);
        testEntityManager.flush();

        // Then
        var actualPerson = personRepository.findByUserIdAndFetchAllAssociations(expectedPerson.getUserId());

        assertThat(actualPerson).isNotSameAs(expectedPerson);
        assertThat(actualPerson.getFirstName()).isEqualTo(expectedPerson.getFirstName());
        assertThat(actualPerson.getSecondName()).isEqualTo(expectedPerson.getSecondName());
        assertThat(actualPerson.getLastName()).isEqualTo(expectedPerson.getLastName());
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(expectedPerson.getDateOfBirth());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(expectedPerson.getEmailAddress());
        assertThat(actualPerson.getPassword()).isEqualTo(expectedPerson.getPassword());
        assertThat(actualPerson.getUpdatedAt()).isAfter(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isNotEmpty();
        assertThat(actualPerson.getVersion()).isEqualTo(expectedPerson.getVersion() + 1);
    }

    @Before
    public void setUp() {
        var referenceDataFactory = new RepositoryReferenceDataFactory(actionRepository, stateRepository, roleRepository, countryRepository);
        testDataFactory = new TestDataFactory(referenceDataFactory);
    }

    @Test
    public void when_creating_a_person_then_a_new_person_is_persisted_to_the_database() {
        // Given
        var expectedPerson = testDataFactory.buildPersonWithAllAssociations();

        // When
        personRepository.save(expectedPerson);
        testEntityManager.flush(); // Force an update from the JPA cache to the database

        // Then
        testEntityManager.clear(); // Clear the JPA cache to guarantee that subsequent finders actually access the database
        var actualPerson = personRepository.findByUserIdAndFetchAllAssociations(expectedPerson.getUserId());

        assertThat(actualPerson).isNotSameAs(expectedPerson);
        assertThat(actualPerson.getCreatedAt()).isNotNull();
        assertThat(actualPerson.getCreatedBy()).isEqualTo(TestDataFactory.JUNIT_TEST_USERNAME);
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(expectedPerson.getDateOfBirth());
        assertThat(actualPerson.getDateOfExpiry()).isEqualTo(expectedPerson.getDateOfExpiry());
        assertThat(actualPerson.getDateOfPasswordExpiry()).isEqualTo(expectedPerson.getDateOfPasswordExpiry());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(expectedPerson.getEmailAddress());
        assertThat(actualPerson.getFirstName()).isEqualTo(expectedPerson.getFirstName());
        assertThat(actualPerson.getHomeAddress()).isEqualTo(expectedPerson.getHomeAddress());
        assertThat(actualPerson.getId()).isNotNull();
        assertThat(actualPerson.getLastName()).isEqualTo(expectedPerson.getLastName());
        assertThat(actualPerson.getPassword()).isEqualTo(expectedPerson.getPassword());
        assertThat(actualPerson.getPhotographs()).containsExactlyElementsOf(expectedPerson.getPhotographs());
        assertThat(actualPerson.getSecondName()).isEqualTo(expectedPerson.getSecondName());
        assertThat(actualPerson.getState()).isEqualTo(expectedPerson.getState());
        assertThat(actualPerson.getTitle()).isEqualTo(expectedPerson.getTitle());
        assertThat(actualPerson.getUpdatedAt()).isEqualTo(actualPerson.getCreatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(TestDataFactory.JUNIT_TEST_USERNAME);
        assertThat(actualPerson.getUserId()).isEqualTo(expectedPerson.getUserId());
        assertThat(actualPerson.getVersion()).isEqualTo(0);
        assertThat(actualPerson.getWorkAddress()).isEqualTo(expectedPerson.getWorkAddress());
        assertThat(actualPerson.getWorkAddress()).isEqualTo(expectedPerson.getWorkAddress());
    }

    @Test
    @Rollback
    public void when_creating_an_invalid_person_then_a_constraint_violation_exception_is_thrown() {
        // Given
        var person = new Person("userId");

        // When
        Throwable throwable = catchThrowable(() -> personRepository.save(person));

        // Then
        if (throwable instanceof TransactionSystemException) {
            throwable = ((TransactionSystemException) throwable).getMostSpecificCause(); // Hsql and Oracle only, MySql and H2 will throw ConstraintViolationException
        }
        assertThat(throwable).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void when_deleting_a_person_then_the_person_no_longer_exists() {
        // Given
        var person = personRepository.save(testDataFactory.buildPersonWithAllAssociations());

        // When
        personRepository.delete(person);

        // Then
        var deletedPerson = personRepository.findById(person.getId());
        assertThat(deletedPerson).isNotPresent();
    }
}
