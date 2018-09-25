package com.gohenry.utils.jpa.repository;

import com.gohenry.utils.jpa.domain.CustomJoinColumnReferencedEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomJoinColumnReferencedRepository extends JpaRepository<CustomJoinColumnReferencedEntity, String> {

}
