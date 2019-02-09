package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;

import java.util.Set;

@RepositoryRestResource
public interface StateRepository extends PagingAndSortingRepository<State, Long> {
    @Query("SELECT state FROM State state LEFT JOIN FETCH state.actions")
    Set<State> findAllWithActions();

    State findByCode(StateCode stateCode);
}

