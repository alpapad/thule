package uk.co.serin.thule.people.repository.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.person.Person;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class PersonRepositoryImplTest {
    @Mock
    private EntityManager entityManager;
    private PersonRepositoryImpl personRepositoryImpl = new PersonRepositoryImpl();
    @Mock
    private TypedQuery<Person> typedQuery;

    @Test
    public void findByCriteria() {
        // Given
        Person expectedPerson = new Person("userId").setEmailAddress("test@gmail.com").setFirstName("firstName").setSurname("surname");

        given(entityManager.<Person>createQuery(anyString(), anyObject())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), anyObject())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(Collections.singletonList(expectedPerson));

        // When
        List<Person> actualPeople = personRepositoryImpl.findByCriteria(expectedPerson.getEmailAddress(), expectedPerson.getFirstName(), expectedPerson.getSurname(), expectedPerson.getUserId());

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void findByNullCriteria() {
        // Given
        Person expectedPerson = new Person("userId");
        ReflectionTestUtils.setField(expectedPerson, DomainModel.ENTITY_ATTRIBUTE_NAME_USER_ID, null);

        given(entityManager.<Person>createQuery(anyString(), anyObject())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), anyObject())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(Collections.singletonList(expectedPerson));

        // When
        List<Person> actualPeople = personRepositoryImpl.findByCriteria(expectedPerson.getEmailAddress(), expectedPerson.getFirstName(), expectedPerson.getSurname(), expectedPerson.getUserId());

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void search() {
        // Given
        Person expectedPerson = new Person("userId");

        given(entityManager.<Person>createQuery(anyString(), anyObject())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), anyObject())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(Collections.singletonList(expectedPerson));

        // When
        List<Person> actualPeople = personRepositoryImpl.search("anything");

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void searchByNullCriteria() {
        // Given
        Person expectedPerson = new Person("userId");

        given(entityManager.<Person>createQuery(anyString(), anyObject())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), anyObject())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(Collections.singletonList(expectedPerson));

        // When
        List<Person> actualPeople = personRepositoryImpl.search(null);

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(personRepositoryImpl, "entityManager", entityManager);
    }
}