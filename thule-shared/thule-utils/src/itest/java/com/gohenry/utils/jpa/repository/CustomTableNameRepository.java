package com.gohenry.utils.jpa.repository;

import com.gohenry.utils.jpa.domain.CustomTableNameEntity;

import org.springframework.data.repository.CrudRepository;

public interface CustomTableNameRepository extends CrudRepository<CustomTableNameEntity, String> {
}
