package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.state.Action;

import java.util.Set;

@RepositoryRestResource
public interface ActionRepository extends PagingAndSortingRepository<Action, Long> {
    @Query("SELECT action FROM Action action LEFT JOIN FETCH action.nextState")
    Set<Action> findAllWithNextState();
}