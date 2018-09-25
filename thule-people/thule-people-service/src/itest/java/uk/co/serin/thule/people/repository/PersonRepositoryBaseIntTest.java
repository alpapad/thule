package uk.co.serin.thule.people.repository;

import com.gohenry.oauth2jpa.SpringSecurityAuditorAware;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.TransactionSystemException;

import uk.co.serin.thule.people.datafactory.ReferenceDataFactory;
import uk.co.serin.thule.people.datafactory.RepositoryReferenceDataFactory;
import uk.co.serin.thule.people.datafactory.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.address.HomeAddress;
import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.person.Photograph;
import uk.co.serin.thule.people.repository.repositories.ActionRepository;
import uk.co.serin.thule.people.repository.repositories.CountryRepository;
import uk.co.serin.thule.people.repository.repositories.PersonRepository;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    public void given_a_new_person_when_finding_all_people_then_the_new_person_is_found() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.buildPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.buildPerson(testPerson);

        // When
        List<Person> actualPeople = personRepository.findAll();

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_finding_that_person_by_id_then_the_new_person_is_found() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.buildPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.buildPerson(testPerson);

        // When
        Optional<Person> actualOptionalPerson = personRepository.findById(testPerson.getId());

        // Then
        Person actualPerson = actualOptionalPerson.orElseThrow();
        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_finding_that_person_by_id_then_the_new_person_is_found_with_all_associations() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.buildPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.buildPerson(testPerson);

        // When
        Person actualPerson = personRepository.findByIdAndFetchAllAssociations(testPerson.getId());

        // Then
        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_finding_that_person_by_updatedBy_then_the_new_person_is_found() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.buildPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.buildPerson(testPerson);

        // When
        Set<Person> actualPeople = personRepository.findByUpdatedBy(TestDataFactory.JUNIT_TEST_USERNAME);

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_finding_that_person_by_userid_then_the_new_person_is_found() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.buildPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.buildPerson(testPerson);

        // When
        Person actualPerson = personRepository.findByUserIdAndFetchAllAssociations(testPerson.getUserId());

        // Then
        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_finding_that_person_with_criteria_then_the_new_person_is_found() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.buildPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.buildPerson(testPerson);

        // When
        List<Person> actualPeople = personRepository.findByCriteria(testPerson.getEmailAddress().substring(1), null, null, null);

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_searching_by_email_address_then_the_new_person_is_found() {
        // Given
        Person testPerson = personRepository.save(testDataFactory.buildPersonWithAllAssociations());
        Person expectedPerson = testDataFactory.buildPerson(testPerson);

        // When
        List<Person> actualPeople = personRepository.search(testPerson.getEmailAddress().substring(1));

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_updating_that_person_then_the_new_person_is_found_with_updated_fields() throws InterruptedException {
        // Given
        Person testPerson = personRepository.save(testDataFactory.buildPersonWithAllAssociations());
        testPerson.setFirstName("updatedFirstName");
        testPerson.setSecondName("updatedSecondName");
        testPerson.setLastName("updatedLastName");
        testPerson.setDateOfBirth(testPerson.getDateOfBirth().minusDays(1));
        testPerson.setEmailAddress("updated@gmail.com");
        testPerson.setPassword("updatedPassword");

        Person expectedPerson = testDataFactory.buildPerson(testPerson);
        ReflectionTestUtils.setField(expectedPerson, DomainModel.ENTITY_ATTRIBUTE_NAME_VERSION, testPerson.getVersion() + 1);

        Thread.sleep(100L); // Allow enough time to lapse for the updatedAt to be updated with a different value

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

    @Before
    public void setUp() {
        ReferenceDataFactory referenceDataFactory = new RepositoryReferenceDataFactory(actionRepository, stateRepository, roleRepository, countryRepository);
        testDataFactory = new TestDataFactory(referenceDataFactory);
    }

    @Test
    public void when_creating_a_person_then_a_new_person_is_created() {
        // Given
        Person testPerson = testDataFactory.buildPersonWithAllAssociations();
        Person expectedPerson = testDataFactory.buildPerson(testPerson);
        ReflectionTestUtils.setField(expectedPerson, DomainModel.ENTITY_ATTRIBUTE_NAME_VERSION, 0L);

        // When
        personRepository.save(testPerson);

        // Then
        Person actualPerson = personRepository.findByUserIdAndFetchAllAssociations(testPerson.getUserId());

        assertThat(actualPerson.getId()).isNotNull();
        assertThat(actualPerson.getUpdatedAt()).isNotNull();
        assertThat(actualPerson.getCreatedAt()).isEqualTo(actualPerson.getUpdatedAt());
        assertThat(actualPerson.getCreatedBy()).isNotNull();
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
                DomainModel.ENTITY_ATTRIBUTE_NAME_CREATED_BY,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);
    }

    @Test
    @Rollback
    public void when_creating_an_invalid_person_then_a_constraint_violation_exception_is_thrown() {
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
    public void when_deleting_a_person_then_the_person_no_longer_exists() {
        // Given
        Person person = personRepository.save(testDataFactory.buildPersonWithAllAssociations());

        // When
        personRepository.delete(person);

        // Then
        Optional<Person> deletedPerson = personRepository.findById(person.getId());
        assertThat(deletedPerson).isNotPresent();
    }

    @EnableJpaAuditing
    @TestConfiguration
    static class IdtFileRepositoryBaseIntTestConfiguration {
    }
}
