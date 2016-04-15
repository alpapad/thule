package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;

@Projection(name = "summary", types = {Role.class})
public interface RoleProjection {
    RoleCode getCode();

    String getDescription();
}
