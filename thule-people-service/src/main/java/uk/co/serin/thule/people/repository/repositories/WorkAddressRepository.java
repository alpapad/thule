package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.rest.projection.WorkAddressProjection;

@RepositoryRestResource(excerptProjection = WorkAddressProjection.class)
public interface WorkAddressRepository extends PagingAndSortingRepository<WorkAddress, Long> {
}

