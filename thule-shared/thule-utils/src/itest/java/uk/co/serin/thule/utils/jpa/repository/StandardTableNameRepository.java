package uk.co.serin.thule.utils.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import uk.co.serin.thule.utils.jpa.domain.StandardTableNameEntity;

public interface StandardTableNameRepository extends CrudRepository<StandardTableNameEntity, String> {
}
