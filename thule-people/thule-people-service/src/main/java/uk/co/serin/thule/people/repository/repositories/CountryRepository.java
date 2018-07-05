package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.country.Country;

@RepositoryRestResource
public interface CountryRepository extends PagingAndSortingRepository<Country, Long> {
}
