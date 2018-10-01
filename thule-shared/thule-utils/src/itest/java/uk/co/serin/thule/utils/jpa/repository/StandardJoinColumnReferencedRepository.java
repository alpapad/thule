package uk.co.serin.thule.utils.jpa.repository;

import uk.co.serin.thule.utils.jpa.domain.StandardJoinColumnReferencedEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardJoinColumnReferencedRepository extends JpaRepository<StandardJoinColumnReferencedEntity, String> {

}
