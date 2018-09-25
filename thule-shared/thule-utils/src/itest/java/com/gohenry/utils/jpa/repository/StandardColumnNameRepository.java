package com.gohenry.utils.jpa.repository;

import com.gohenry.utils.jpa.domain.StandardColumnNameEntity;

import org.springframework.data.repository.CrudRepository;

public interface StandardColumnNameRepository extends CrudRepository<StandardColumnNameEntity, String> {

    StandardColumnNameEntity findBySomeProperty(String value);
}
