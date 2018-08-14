package uk.co.serin.thule.repository.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import uk.co.serin.thule.repository.mongodb.domain.Person;

import java.util.Optional;

public interface PersonRepository extends MongoRepository<Person, Long> {
    Optional<Person> findByUserId(String userId);
}
