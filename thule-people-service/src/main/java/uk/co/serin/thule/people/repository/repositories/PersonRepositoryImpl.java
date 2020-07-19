package uk.co.serin.thule.people.repository.repositories;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.utils.trace.TracePublicMethods;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@TracePublicMethods
public class PersonRepositoryImpl implements PersonRepositoryCustom {
    private static final String ENTITY_ATTRIBUTE_NAME_EMAIL_ADDRESS = "emailAddress";
    private static final String ENTITY_ATTRIBUTE_NAME_FIRST_NAME = "firstName";
    private static final String ENTITY_ATTRIBUTE_NAME_LAST_NAME = "lastName";
    private static final String ENTITY_ATTRIBUTE_NAME_USER_ID = "userId";
    private static final String FIND_PEOPLE_BY_CRITERIA_JPA_QL =
            "select DISTINCT(person) FROM PersonEntity person LEFT JOIN FETCH person.roles roles LEFT JOIN FETCH person.accounts accounts LEFT JOIN FETCH person.state state LEFT JOIN FETCH state.actions actions WHERE person.id > 0 and ( person.emailAddress like :emailAddress or :emailAddress is null) and ( person.firstName like :firstName or :firstName is null) and ( person.lastName like :lastName or :lastName is null) and ( person.userId like :userId or :userId is null)";
    private static final String SEARCH_PEOPLE_BY_QUERY_JPA_QL =
            "select DISTINCT(person) FROM PersonEntity person LEFT JOIN FETCH person.roles roles LEFT JOIN FETCH person.accounts accounts LEFT JOIN FETCH person.state state LEFT JOIN FETCH state.actions actions WHERE person.id > 0 and ( person.emailAddress like :emailAddress or person.firstName like :firstName or person.lastName like :lastName or person.userId like :userId)";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PersonEntity> findByCriteria(String emailAddress, String firstName, String lastName, String userId) {
        var emailAddressForLikeComparison = (emailAddress == null) ? null : '%' + emailAddress + '%';
        var firstNameForLikeComparison = (firstName == null) ? null : '%' + firstName + '%';
        var lastNameForLikeComparison = (lastName == null) ? null : '%' + lastName + '%';
        var userIdForLikeComparison = (userId == null) ? null : '%' + userId + '%';

        return entityManager.createQuery(FIND_PEOPLE_BY_CRITERIA_JPA_QL, PersonEntity.class)
                            .setParameter(ENTITY_ATTRIBUTE_NAME_EMAIL_ADDRESS, emailAddressForLikeComparison)
                            .setParameter(ENTITY_ATTRIBUTE_NAME_FIRST_NAME, firstNameForLikeComparison)
                            .setParameter(ENTITY_ATTRIBUTE_NAME_LAST_NAME, lastNameForLikeComparison)
                            .setParameter(ENTITY_ATTRIBUTE_NAME_USER_ID, userIdForLikeComparison).getResultList();
    }

    @Override
    public List<PersonEntity> search(String searchQuery) {
        var queryForLikeComparison = (searchQuery == null) ? null : '%' + searchQuery + '%';

        return entityManager.createQuery(SEARCH_PEOPLE_BY_QUERY_JPA_QL, PersonEntity.class)
                            .setParameter(ENTITY_ATTRIBUTE_NAME_EMAIL_ADDRESS, queryForLikeComparison)
                            .setParameter(ENTITY_ATTRIBUTE_NAME_FIRST_NAME, queryForLikeComparison)
                            .setParameter(ENTITY_ATTRIBUTE_NAME_LAST_NAME, queryForLikeComparison)
                            .setParameter(ENTITY_ATTRIBUTE_NAME_USER_ID, queryForLikeComparison).getResultList();
    }
}