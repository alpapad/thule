package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.address.HomeAddress;
import uk.co.serin.thule.people.rest.projection.HomeAddressProjection;

@RepositoryRestResource(excerptProjection = HomeAddressProjection.class)
public interface HomeAddressRepository extends PagingAndSortingRepository<HomeAddress, Long> {
}

