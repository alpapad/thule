package uk.co.serin.thule.people.repository.repositories;

import uk.co.serin.thule.utils.aop.TracePublicMethods;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.person.Person;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TracePublicMethods
public class PersonRepositoryImpl implements PersonRepositoryCustom {
    private static final String FIND_PEOPLE_BY_CRITERIA_EJB_QL =
            "select DISTINCT(person) FROM Person person LEFT JOIN FETCH person.roles roles LEFT JOIN FETCH person.photographs photographs LEFT JOIN FETCH person.state state LEFT JOIN FETCH state.actions actions WHERE person.id > 0 and ( person.emailAddress like :emailAddress or :emailAddress is null) and ( person.firstName like :firstName or :firstName is null) and ( person.lastName like :lastName or :lastName is null) and ( person.userId like :userId or :userId is null)";
    private static final String SEARCH_PEOPLE_BY_QUERY_EJB_QL =
            "select DISTINCT(person) FROM Person person LEFT JOIN FETCH person.roles roles LEFT JOIN FETCH person.photographs photographs LEFT JOIN FETCH person.state state LEFT JOIN FETCH state.actions actions WHERE person.id > 0 and ( person.emailAddress like :emailAddress or person.firstName like :firstName or person.lastName like :lastName or person.userId like :userId)";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Person> findByCriteria(String emailAddress, String firstName, String lastName, String userId) {
        String emailAddressForLikeComparison = (emailAddress == null) ? null : '%' + emailAddress + '%';
        String firstNameForLikeComparison = (firstName == null) ? null : '%' + firstName + '%';
        String lastNameForLikeComparison = (lastName == null) ? null : '%' + lastName + '%';
        String userIdForLikeComparison = (userId == null) ? null : '%' + userId + '%';

        return entityManager.createQuery(FIND_PEOPLE_BY_CRITERIA_EJB_QL, Person.class)
                .setParameter(DomainModel.ENTITY_ATTRIBUTE_NAME_EMAIL_ADDRESS, emailAddressForLikeComparison)
                .setParameter(DomainModel.ENTITY_ATTRIBUTE_NAME_FIRST_NAME, firstNameForLikeComparison)
                .setParameter(DomainModel.ENTITY_ATTRIBUTE_NAME_LAST_NAME, lastNameForLikeComparison)
                .setParameter(DomainModel.ENTITY_ATTRIBUTE_NAME_USER_ID, userIdForLikeComparison).getResultList();
    }

    @Override
    public List<Person> search(String searchQuery) {
        String queryForLikeComparison = (searchQuery == null) ? null : '%' + searchQuery + '%';

        return entityManager.createQuery(SEARCH_PEOPLE_BY_QUERY_EJB_QL, Person.class)
                .setParameter(DomainModel.ENTITY_ATTRIBUTE_NAME_EMAIL_ADDRESS, queryForLikeComparison)
                .setParameter(DomainModel.ENTITY_ATTRIBUTE_NAME_FIRST_NAME, queryForLikeComparison)
                .setParameter(DomainModel.ENTITY_ATTRIBUTE_NAME_LAST_NAME, queryForLikeComparison)
                .setParameter(DomainModel.ENTITY_ATTRIBUTE_NAME_USER_ID, queryForLikeComparison).getResultList();
    }
}