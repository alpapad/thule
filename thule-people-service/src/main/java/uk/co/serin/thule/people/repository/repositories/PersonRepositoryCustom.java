package uk.co.serin.thule.people.repository.repositories;

import uk.co.serin.thule.people.domain.person.Person;

import java.util.List;

public interface PersonRepositoryCustom {
    List<Person> findByCriteria(String emailAddress, String firstName, String surname, String userId);

    List<Person> search(String searchQuery);
}
