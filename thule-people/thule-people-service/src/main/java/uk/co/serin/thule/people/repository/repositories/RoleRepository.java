package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;

@RepositoryRestResource
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
    Role findByCode(RoleCode roleCode);
}