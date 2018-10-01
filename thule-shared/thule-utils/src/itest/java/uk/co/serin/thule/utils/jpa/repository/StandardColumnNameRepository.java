package uk.co.serin.thule.utils.jpa.repository;

import uk.co.serin.thule.utils.jpa.domain.StandardColumnNameEntity;

import org.springframework.data.repository.CrudRepository;

public interface StandardColumnNameRepository extends CrudRepository<StandardColumnNameEntity, String> {

    StandardColumnNameEntity findBySomeProperty(String value);
}
