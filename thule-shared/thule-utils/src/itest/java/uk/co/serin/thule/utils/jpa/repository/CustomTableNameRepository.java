package uk.co.serin.thule.utils.jpa.repository;

import uk.co.serin.thule.utils.jpa.domain.CustomTableNameEntity;

import org.springframework.data.repository.CrudRepository;

public interface CustomTableNameRepository extends CrudRepository<CustomTableNameEntity, String> {
}
