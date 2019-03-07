package uk.co.serin.thule.repository.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import uk.co.serin.thule.repository.mongodb.domain.entity.PersonEntity;

import java.util.Optional;

public interface PersonRepository extends MongoRepository<PersonEntity, Long> {
    Optional<PersonEntity> findByUserId(String userId);
}
