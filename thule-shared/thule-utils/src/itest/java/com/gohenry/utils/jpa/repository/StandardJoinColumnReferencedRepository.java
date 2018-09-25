package com.gohenry.utils.jpa.repository;

import com.gohenry.utils.jpa.domain.StandardJoinColumnReferencedEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardJoinColumnReferencedRepository extends JpaRepository<StandardJoinColumnReferencedEntity, String> {

}
