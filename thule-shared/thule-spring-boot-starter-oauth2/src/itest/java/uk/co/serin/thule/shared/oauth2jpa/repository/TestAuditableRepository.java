package uk.co.serin.thule.shared.oauth2jpa.repository;

import uk.co.serin.thule.shared.oauth2jpa.domain.TestAuditable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAuditableRepository extends JpaRepository<TestAuditable, Long> {
}
