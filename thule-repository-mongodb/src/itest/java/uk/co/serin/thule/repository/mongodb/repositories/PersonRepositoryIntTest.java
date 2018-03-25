package uk.co.serin.thule.repository.mongodb.repositories;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.repository.mongodb.MongoDockerContainer;
import uk.co.serin.thule.repository.mongodb.domain.Person;
import uk.co.serin.thule.repository.mongodb.domain.PersonFactory;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PersonRepositoryIntTest {
    @Autowired
    private Environment env;
    @Autowired
    private PersonRepository personRepository;

    @BeforeClass
    public static void setUpClass() {
        MongoDockerContainer.startRabbitContainerIfDown();
    }

    @AfterClass
    public static void tearDownClass() {
        MongoDockerContainer.stopMongoContainerIfUp();
    }

    @Test
    public void create() {
        // Given
        Person testPerson = PersonFactory.newPerson();

        Person expectedPerson = PersonFactory.newPerson(testPerson);
        expectedPerson.setVersion(0L);

        // When
        personRepository.save(testPerson);

        // Then
        Person actualPerson = personRepository.findByUserId(expectedPerson.getUserId());
        assertThat(actualPerson).isNotSameAs(expectedPerson);

        assertThat(actualPerson.getCreatedAt()).isEqualTo(actualPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedAt()).isNotNull();
        assertThat(actualPerson.getUpdatedBy()).isNotEmpty();

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson,
                Person.ENTITY_ATTRIBUTE_NAME_CREATED_AT,
                Person.ENTITY_ATTRIBUTE_NAME_UPDATED_AT,
                Person.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);
    }

    @Test(expected = ConstraintViolationException.class)
    public void create_a_person_violating_validation_constraints() {
        // Given
        Person person = new Person("userId");
        person.setId(1L);

        // When
        personRepository.save(person);

        // Then (see expected in @Test annotation)
    }

    @Test
    public void delete() {
        // Given
        Person person = personRepository.save(PersonFactory.newPerson());

        // When
        personRepository.delete(person);

        // Then
        Optional<Person> deletedPerson = personRepository.findById(person.getId());
        assertThat(deletedPerson).isNotPresent();
    }

    @Test
    public void find_all() {
        // Given
        Person expectedPerson = personRepository.save(PersonFactory.newPerson());

        // When
        Iterable<Person> actualPeople = personRepository.findAll();

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void find_by_id() {
        // Given
        Person expectedPerson = personRepository.save(PersonFactory.newPerson());

        // When
        Optional<Person> actualPerson = personRepository.findById(expectedPerson.getId());

        // Then
        assertThat(actualPerson).isPresent();
        assertThat(actualPerson.get()).isNotSameAs(expectedPerson);
        assertThat(actualPerson.get()).isEqualToComparingFieldByField(expectedPerson);
    }

    @Test
    public void find_by_userid() {
        // Given
        Person expectedPerson = personRepository.save(PersonFactory.newPerson());

        // When
        Person actualPerson = personRepository.findByUserId(expectedPerson.getUserId());

        // Then
        assertThat(actualPerson).isNotSameAs(expectedPerson);
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Before
    public void setUp() {
        MongoDockerContainer rabbitDockerContainer = new MongoDockerContainer(env);
        rabbitDockerContainer.waitForMongoAvailabilty();
        personRepository.deleteAll();
    }

    @Test
    public void update() throws InterruptedException {
        // Given
        Person testPerson = personRepository.save(PersonFactory.newPerson());
        testPerson.setDateOfBirth(testPerson.getDateOfBirth().minusDays(1));
        testPerson.setEmailAddress("updated@serin-consultancy.co.uk");
        testPerson.setFirstName("updatedFirstName");
        testPerson.setLastName("updatedLastName");
        testPerson.setPassword("updatedPassword");
        testPerson.setSecondName("updatedSecondName");

        Person expectedPerson = PersonFactory.newPerson(testPerson);
        expectedPerson.setVersion(testPerson.getVersion() + 1);

        Thread.sleep(10L); // Allow enough time to lapse for the updatedAt to be updated with a different value

        // When
        personRepository.save(testPerson);

        // Then
        Person actualPerson = personRepository.findByUserId(expectedPerson.getUserId());
        assertThat(actualPerson).isNotSameAs(expectedPerson);

        assertThat(actualPerson.getUpdatedAt()).isAfter(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isNotEmpty();
        assertThat(actualPerson.getUpdatedBy()).isNotEqualTo(expectedPerson.getUpdatedBy());

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson,
                Person.ENTITY_ATTRIBUTE_NAME_CREATED_AT, // delete this line once the fix for https://jira.spring.io/browse/DATAMONGO-1639 has been released in version 1.10.2
                Person.ENTITY_ATTRIBUTE_NAME_UPDATED_AT,
                Person.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);
    }
}
