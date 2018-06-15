package uk.co.serin.thule.repository.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import uk.co.serin.thule.repository.mongodb.domain.Person;

public interface PersonRepository extends MongoRepository<Person, Long> {
    Person findByUserId(String userId);
}
