package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.entity.role.RoleCode;
import uk.co.serin.thule.people.domain.entity.role.RoleEntity;

@Projection(name = "summary", types = {RoleEntity.class})
public interface RoleProjection {
    RoleCode getCode();

    String getDescription();
}
