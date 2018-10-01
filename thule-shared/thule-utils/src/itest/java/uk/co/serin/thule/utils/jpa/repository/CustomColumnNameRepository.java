package uk.co.serin.thule.utils.jpa.repository;

import uk.co.serin.thule.utils.jpa.domain.CustomColumnNameEntity;

import org.springframework.data.repository.CrudRepository;

public interface CustomColumnNameRepository extends CrudRepository<CustomColumnNameEntity, String> {

    CustomColumnNameEntity findBySomeProperty(String value);
}