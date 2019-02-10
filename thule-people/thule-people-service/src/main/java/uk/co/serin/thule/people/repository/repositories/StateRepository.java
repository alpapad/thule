package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.entity.state.StateEntity;
import uk.co.serin.thule.people.domain.model.state.StateCode;

import java.util.Set;

@RepositoryRestResource
public interface StateRepository extends PagingAndSortingRepository<StateEntity, Long> {
    @Query("SELECT state FROM StateEntity state LEFT JOIN FETCH state.actions")
    Set<StateEntity> findAllWithActions();

    StateEntity findByCode(StateCode stateCode);
}

