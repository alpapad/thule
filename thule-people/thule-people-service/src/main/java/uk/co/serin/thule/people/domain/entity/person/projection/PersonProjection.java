package uk.co.serin.thule.people.domain.entity.person.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.entity.role.projection.RoleProjection;
import uk.co.serin.thule.people.domain.entity.state.projection.StateProjection;

import java.time.LocalDate;
import java.util.Set;

@Projection(name = "summary", types = {PersonEntity.class})
public interface PersonProjection {
    LocalDate getDateOfBirth();

    LocalDate getDateOfExpiry();

    LocalDate getDateOfPasswordExpiry();

    String getEmailAddress();

    String getFirstName();

    String getLastName();

    String getPassword();

    Set<RoleProjection> getRoles();

    String getSecondName();

    StateProjection getState();

    String getTitle();

    String getUserId();
}