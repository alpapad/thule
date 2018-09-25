package com.gohenry.utils.jpa.repository;

import com.gohenry.utils.jpa.domain.CustomColumnNameEntity;

import org.springframework.data.repository.CrudRepository;

public interface CustomColumnNameRepository extends CrudRepository<CustomColumnNameEntity, String> {

    CustomColumnNameEntity findBySomeProperty(String value);
}