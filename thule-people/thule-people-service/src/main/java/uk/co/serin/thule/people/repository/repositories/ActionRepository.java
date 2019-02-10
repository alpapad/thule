package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.entity.state.ActionEntity;

import java.util.Set;

@RepositoryRestResource
public interface ActionRepository extends PagingAndSortingRepository<ActionEntity, Long> {
    @Query("SELECT action FROM ActionEntity action LEFT JOIN FETCH action.nextState")
    Set<ActionEntity> findAllWithNextState();
}