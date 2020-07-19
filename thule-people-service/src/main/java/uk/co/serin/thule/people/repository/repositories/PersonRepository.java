package uk.co.serin.thule.people.repository.repositories;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;

import java.util.Set;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends JpaRepository<PersonEntity, Long>, PersonRepositoryCustom {
    @Query("SELECT person FROM PersonEntity person LEFT JOIN FETCH person.roles roles LEFT JOIN FETCH person.accounts accounts LEFT JOIN FETCH person.state state LEFT JOIN FETCH state.actions actions  WHERE person.id = :id")
    @NewSpan
    PersonEntity findByIdAndFetchAllAssociations(Long id);

    @Query("SELECT person FROM PersonEntity person LEFT JOIN FETCH person.accounts accounts WHERE person.updatedBy = :updatedBy")
    @NewSpan
    Set<PersonEntity> findByUpdatedBy(String updatedBy);

    @Query("SELECT person FROM PersonEntity person LEFT JOIN FETCH person.roles roles LEFT JOIN FETCH person.accounts accounts LEFT JOIN FETCH person.state state LEFT JOIN FETCH state.actions actions  WHERE person.userId = :userId")
    @NewSpan
    PersonEntity findByUserIdAndFetchAllAssociations(String userId);
}
