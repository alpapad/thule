package uk.co.serin.thule.people.repository.repositories;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.repository.NoRepositoryBean;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;

import java.util.List;

@NoRepositoryBean
public interface PersonRepositoryCustom {
    @NewSpan
    List<PersonEntity> findByCriteria(String emailAddress, String firstName, String lastName, String userId);

    @NewSpan
    List<PersonEntity> search(String searchQuery);
}
