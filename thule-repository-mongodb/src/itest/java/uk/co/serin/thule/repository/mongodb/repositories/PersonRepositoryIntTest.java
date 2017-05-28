package uk.co.serin.thule.repository.mongodb.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.core.utils.RandomGenerators;
import uk.co.serin.thule.repository.mongodb.domain.Person;

import java.time.LocalDate;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonRepositoryIntTest {
    public static final int SUFFIX_LENGTH = 8;
    private static final String EMAIL_ADDRESS_SUFFIX = "@serin-consultancy.co.uk";
    private long id = 0;
    @Autowired
    private PersonRepository personRepository;

    @Test
    public void create() {
        // Given
        Person testPerson = newPerson();

        Person expectedPerson = newPerson(testPerson);
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

    private Person newPerson() {
        // Set the attributes
        final LocalDate dob = RandomGenerators.generateUniqueRandomDateInThePast();
        final LocalDate expiryDate = RandomGenerators.generateUniqueRandomDateInTheFuture();
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(SUFFIX_LENGTH);

        return Person.PersonBuilder.aPerson().
                withDateOfBirth(dob).
                withDateOfExpiry(expiryDate).
                withDateOfPasswordExpiry(expiryDate).
                withEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                withFirstName("Elizabeth").
                withId(id++).
                withPassword(userId).
                withSalutation("Miss").
                withSecondName("K").
                withSurname("Scarlett").
                withUserId(userId).build();
    }

    private Person newPerson(Person person) {
        return Person.PersonBuilder.aPerson().
                withCreatedAt(person.getCreatedAt()).
                withDateOfBirth(person.getDateOfBirth()).
                withDateOfExpiry(person.getDateOfExpiry()).
                withDateOfPasswordExpiry(person.getDateOfPasswordExpiry()).
                withEmailAddress(person.getEmailAddress()).
                withFirstName(person.getFirstName()).
                withId(person.getId()).
                withPassword(person.getPassword()).
                withSalutation(person.getSalutation()).
                withSecondName(person.getSecondName()).
                withSurname(person.getSurname()).
                withUpdatedAt(person.getUpdatedAt()).
                withUpdatedBy(person.getUpdatedBy()).
                withUserId(person.getUserId()).
                withVersion(person.getVersion()).build();
    }

    @Test(expected = ConstraintViolationException.class)
    public void createAPersonViolatingValidationConstraints() {
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
        Person person = personRepository.save(newPerson());

        // When
        personRepository.delete(person);

        // Then
        person = personRepository.findOne(person.getId());
        assertThat(person).isNull();
    }

    @Test
    public void findAll() {
        // Given
        Person expectedPerson = personRepository.save(newPerson());

        // When
        Iterable<Person> actualPeople = personRepository.findAll();

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void findById() {
        // Given
        Person expectedPerson = personRepository.save(newPerson());

        // When
        Person actualPerson = personRepository.findOne(expectedPerson.getId());

        // Then
        assertThat(actualPerson).isNotSameAs(expectedPerson);
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Test
    public void findByUserid() {
        // Given
        Person expectedPerson = personRepository.save(newPerson());

        // When
        Person actualPerson = personRepository.findByUserId(expectedPerson.getUserId());

        // Then
        assertThat(actualPerson).isNotSameAs(expectedPerson);
        assertThat(actualPerson).isEqualToComparingFieldByField(expectedPerson);
    }

    @Before
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    public void update() throws InterruptedException {
        // Given
        Person testPerson = personRepository.save(newPerson());
        testPerson.setDateOfBirth(testPerson.getDateOfBirth().minusDays(1));
        testPerson.setEmailAddress("updated@serin-consultancy.co.uk");
        testPerson.setFirstName("updatedFirstName");
        testPerson.setPassword("updatedPassword");
        testPerson.setSecondName("updatedSecondName");
        testPerson.setSurname("updatedSurname");

        Person expectedPerson = newPerson(testPerson);
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
