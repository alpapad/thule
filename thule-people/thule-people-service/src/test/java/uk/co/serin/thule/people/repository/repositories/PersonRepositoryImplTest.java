package uk.co.serin.thule.people.repository.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.person.Person;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PersonRepositoryImplTest {
    @Mock
    private EntityManager entityManager;
    private PersonRepositoryImpl personRepositoryImpl = new PersonRepositoryImpl();
    @Mock
    private TypedQuery<Person> typedQuery;

    @Test
    public void given_new_person_when_find_by_criteria_then_person_is_found() {
        // Given
        Person expectedPerson = Person.builder().userId("userId").emailAddress("test@gmail.com").firstName("firstName").lastName("lastName").build();

        given(entityManager.<Person>createQuery(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(Collections.singletonList(expectedPerson));

        // When
        List<Person> actualPeople = personRepositoryImpl.findByCriteria(expectedPerson.getEmailAddress(), expectedPerson.getFirstName(), expectedPerson.getLastName(), expectedPerson.getUserId());

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void given_new_person_when_find_by_null_criteria_then_person_is_found() {
        // Given
        Person expectedPerson = Person.builder().userId("userId").build();
        ReflectionTestUtils.setField(expectedPerson, DomainModel.ENTITY_ATTRIBUTE_NAME_USER_ID, null);

        given(entityManager.<Person>createQuery(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(Collections.singletonList(expectedPerson));

        // When
        List<Person> actualPeople = personRepositoryImpl.findByCriteria(expectedPerson.getEmailAddress(), expectedPerson.getFirstName(), expectedPerson.getLastName(), expectedPerson.getUserId());

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void given_new_person_when_search_by_null_criteria_then_person_is_found() {
        // Given
        Person expectedPerson = Person.builder().userId("userId").build();

        given(entityManager.<Person>createQuery(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(Collections.singletonList(expectedPerson));

        // When
        List<Person> actualPeople = personRepositoryImpl.search(null);

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void given_new_person_when_search_then_person_is_found() {
        // Given
        Person expectedPerson = Person.builder().userId("userId").build();

        given(entityManager.<Person>createQuery(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(Collections.singletonList(expectedPerson));

        // When
        List<Person> actualPeople = personRepositoryImpl.search("anything");

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(personRepositoryImpl, "entityManager", entityManager);
    }
}