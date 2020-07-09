package uk.co.serin.thule.people.repository.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PersonEntityRepositoryImplTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private PersonRepositoryImpl personRepositoryImpl;
    @Mock
    private TypedQuery<PersonEntity> typedQuery;

    @Test
    void given_new_person_when_find_by_criteria_then_person_is_found() {
        // Given
        var expectedPerson = PersonEntity.builder().userId("userId").emailAddress("test@gmail.com").firstName("firstName").lastName("lastName").build();

        given(entityManager.<PersonEntity>createQuery(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(List.of(expectedPerson));

        // When
        var actualPeople = personRepositoryImpl
                .findByCriteria(expectedPerson.getEmailAddress(), expectedPerson.getFirstName(), expectedPerson.getLastName(), expectedPerson.getUserId());

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    void given_new_person_when_find_by_null_criteria_then_person_is_found() {
        // Given
        var expectedPerson = PersonEntity.builder().build();

        given(entityManager.<PersonEntity>createQuery(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(List.of(expectedPerson));

        // When
        var actualPeople = personRepositoryImpl
                .findByCriteria(expectedPerson.getEmailAddress(), expectedPerson.getFirstName(), expectedPerson.getLastName(), expectedPerson.getUserId());

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    void given_new_person_when_search_by_null_criteria_then_person_is_found() {
        // Given
        var expectedPerson = PersonEntity.builder().userId("userId").build();

        given(entityManager.<PersonEntity>createQuery(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(List.of(expectedPerson));

        // When
        var actualPeople = personRepositoryImpl.search(null);

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    void given_new_person_when_search_then_person_is_found() {
        // Given
        var expectedPerson = PersonEntity.builder().userId("userId").build();

        given(entityManager.<PersonEntity>createQuery(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), any())).willReturn(typedQuery);
        given(typedQuery.getResultList()).willReturn(List.of(expectedPerson));

        // When
        var actualPeople = personRepositoryImpl.search("anything");

        // Then
        assertThat(actualPeople).contains(expectedPerson);
    }
}