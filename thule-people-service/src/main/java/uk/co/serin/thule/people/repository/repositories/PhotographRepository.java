package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.entity.person.PhotographEntity;

@RepositoryRestResource
public interface PhotographRepository extends PagingAndSortingRepository<PhotographEntity, Long> {
}

