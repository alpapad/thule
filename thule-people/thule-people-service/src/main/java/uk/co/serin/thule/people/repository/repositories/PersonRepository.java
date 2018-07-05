package uk.co.serin.thule.people.repository.repositories;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.repository.support.ThuleRepository;

import java.util.Set;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends ThuleRepository<Person, Long>, PersonRepositoryCustom {
    @Query("SELECT person FROM Person person LEFT JOIN FETCH person.roles roles LEFT JOIN FETCH person.photographs photographs LEFT JOIN FETCH person.state state LEFT JOIN FETCH state.actions actions  WHERE person.id = :id")
    @NewSpan
    Person findByIdAndFetchAllAssociations(@Param("id") Long id);

    @Query("SELECT person FROM Person person LEFT JOIN FETCH person.photographs photographs WHERE person.updatedBy = :updatedBy")
    @NewSpan
    Set<Person> findByUpdatedBy(@Param("updatedBy") String updatedBy);

    @Query("SELECT person FROM Person person LEFT JOIN FETCH person.roles roles LEFT JOIN FETCH person.photographs photographs LEFT JOIN FETCH person.state state LEFT JOIN FETCH state.actions actions  WHERE person.userId = :userId")
    @NewSpan
    Person findByUserIdAndFetchAllAssociations(@Param("userId") String userId);
}
