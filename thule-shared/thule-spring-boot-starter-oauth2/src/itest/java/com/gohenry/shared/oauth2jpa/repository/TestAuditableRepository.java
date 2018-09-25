package com.gohenry.shared.oauth2jpa.repository;

import com.gohenry.shared.oauth2jpa.domain.TestAuditable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAuditableRepository extends JpaRepository<TestAuditable, Long> {
}
