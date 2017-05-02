package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.rest.projection.ActionProjection;

import java.util.Set;

@RepositoryRestResource(excerptProjection = ActionProjection.class)
public interface ActionRepository extends PagingAndSortingRepository<Action, Long> {
    @Query("SELECT a FROM Action a LEFT JOIN FETCH a.nextState")
    Set<Action> findAllWithNextState();
}

