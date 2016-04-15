package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.rest.projection.RoleProjection;

@RepositoryRestResource(excerptProjection = RoleProjection.class)
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
    Role findByCode(RoleCode roleCode);
}

