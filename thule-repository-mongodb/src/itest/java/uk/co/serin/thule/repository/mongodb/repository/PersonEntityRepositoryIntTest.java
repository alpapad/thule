package uk.co.serin.thule.repository.mongodb.repository;

import com.google.gson.Gson;

import org.awaitility.Duration;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.repository.mongodb.domain.PersonEntity;
import uk.co.serin.thule.repository.mongodb.domain.PersonFactory;
import uk.co.serin.thule.utils.docker.DockerCompose;
import uk.co.serin.thule.utils.utils.RandomUtils;

import java.io.IOException;
import java.net.Socket;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

@ActiveProfiles("itest")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PersonEntityRepositoryIntTest {
    private static final String MOCK_USERS_NAME = "user";
    private static DockerCompose dockerCompose = new DockerCompose("src/test/docker/thule-repository-mongodb-tests/docker-compose-mongo.yml");
    @Autowired
    private Environment env;
    private Gson gson = new Gson();
    @Autowired
    private PersonRepository personRepository;

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.up();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Test
    public void given_a_new_person_when_finding_all_people_then_the_new_person_is_found() {
        // Given
        var expectedPerson = createAndPersistPerson();

        // When
        var actualPeople = personRepository.findAll();

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    private PersonEntity createAndPersistPerson() {
        return personRepository.save(PersonFactory.newPerson());
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
        var actualPerson = personRepository.findById(expectedPerson.getId()).orElseThrow();

        // Then
        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Test
    public void given_a_new_person_when_finding_that_person_by_userid_then_the_new_person_is_found() {
        // Given
        var expectedPerson = createAndPersistPerson();

        // When
        var actualPerson = personRepository.findByUserId(expectedPerson.getUserId()).orElseThrow();

        // Then
        assertThat(actualPerson).isEqualTo(expectedPerson);
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

        var testPerson = gson.fromJson(gson.toJson(expectedPerson), PersonEntity.class);

        Thread.sleep(10L); // Allow enough time to lapse for the updatedAt to be updated with a different value

        // When
        personRepository.save(testPerson);

        // Then
        var actualPerson = personRepository.findByUserId(expectedPerson.getUserId()).orElseThrow();

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
        // Ideally, this would be done as a static @BeforeClass method. However, we need access to
        // the spring environment which is autowired and hence cannot be static
        var mongodbHost = env.getProperty("thule.repositorymongodb.mongodb.host", "localhost");
        var mongodbPort = env.getProperty("thule.repositorymongodb.mongodb.port", Integer.class, 27017);

        // Wait until MongoDb is up by checking that the port is available
        given().ignoreExceptions().pollInterval(fibonacci()).
                await().timeout(Duration.FIVE_MINUTES).
                       untilAsserted(() -> new Socket(mongodbHost, mongodbPort).close());
    }

    @Test
    public void when_creating_a_person_then_a_new_person_is_persisted_to_the_database() {
        // Given
        var expectedPerson = PersonFactory.newPerson();

        // When
        personRepository.save(expectedPerson);

        // Then
        var actualPerson = personRepository.findByUserId(expectedPerson.getUserId()).orElseThrow();

        assertThat(actualPerson).isNotSameAs(expectedPerson);
        assertThat(actualPerson.getCreatedAt()).isNotNull();
        assertThat(actualPerson.getCreatedBy()).isEqualTo(MOCK_USERS_NAME);
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(expectedPerson.getDateOfBirth());
        assertThat(actualPerson.getDateOfExpiry()).isEqualTo(expectedPerson.getDateOfExpiry());
        assertThat(actualPerson.getDateOfPasswordExpiry()).isEqualTo(expectedPerson.getDateOfPasswordExpiry());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(expectedPerson.getEmailAddress());
        assertThat(actualPerson.getFirstName()).isEqualTo(expectedPerson.getFirstName());
        assertThat(actualPerson.getId()).isNotNull();
        assertThat(actualPerson.getLastName()).isEqualTo(expectedPerson.getLastName());
        assertThat(actualPerson.getPassword()).isEqualTo(expectedPerson.getPassword());
        assertThat(actualPerson.getSecondName()).isEqualTo(expectedPerson.getSecondName());
        assertThat(actualPerson.getTitle()).isEqualTo(expectedPerson.getTitle());
        assertThat(actualPerson.getUpdatedAt()).isEqualTo(actualPerson.getCreatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(MOCK_USERS_NAME);
        assertThat(actualPerson.getUserId()).isEqualTo(expectedPerson.getUserId());
        assertThat(actualPerson.getVersion()).isEqualTo(1);
    }

    @Test
    public void when_creating_an_invalid_person_then_a_constraint_violation_exception_is_thrown() {
        // Given
        var person = PersonEntity.builder().id(RandomUtils.generateUniqueRandomLong()).userId("userId").build();

        // When
        var throwable = catchThrowable(() -> personRepository.save(person));

        // Then
        assertThat(throwable).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void when_deleting_a_person_then_the_person_no_longer_exists() {
        // Given
        var person = createAndPersistPerson();

        // When
        personRepository.delete(person);

        // Then
        var deletedPerson = personRepository.findById(person.getId());
        assertThat(deletedPerson).isNotPresent();
    }
}
