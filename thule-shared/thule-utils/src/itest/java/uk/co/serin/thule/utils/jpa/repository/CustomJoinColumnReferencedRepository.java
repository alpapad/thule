package uk.co.serin.thule.utils.jpa.repository;

import uk.co.serin.thule.utils.jpa.domain.CustomJoinColumnReferencedEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomJoinColumnReferencedRepository extends JpaRepository<CustomJoinColumnReferencedEntity, String> {

}
