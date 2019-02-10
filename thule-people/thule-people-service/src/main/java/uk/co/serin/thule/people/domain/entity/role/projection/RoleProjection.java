package uk.co.serin.thule.people.domain.entity.role.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.entity.role.RoleEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleCode;

@Projection(name = "summary", types = {RoleEntity.class})
public interface RoleProjection {
    RoleCode getCode();

    String getDescription();
}
