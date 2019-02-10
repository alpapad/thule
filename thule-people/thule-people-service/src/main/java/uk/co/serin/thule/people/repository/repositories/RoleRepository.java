package uk.co.serin.thule.people.repository.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.co.serin.thule.people.domain.entity.role.RoleEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleCode;

@RepositoryRestResource
public interface RoleRepository extends PagingAndSortingRepository<RoleEntity, Long> {
    RoleEntity findByCode(RoleCode roleCode);
}