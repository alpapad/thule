package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.entity.state.ActionEntity;
import uk.co.serin.thule.people.domain.model.state.ActionCode;

@Projection(name = "summary", types = {ActionEntity.class})
public interface ActionProjection {
    ActionCode getCode();

    String getDescription();
}
