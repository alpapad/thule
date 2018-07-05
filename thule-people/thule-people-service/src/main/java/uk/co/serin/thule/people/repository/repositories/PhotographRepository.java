package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.person.Photograph;

@RepositoryRestResource
public interface PhotographRepository extends PagingAndSortingRepository<Photograph, Long> {
}

