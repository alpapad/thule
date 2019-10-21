package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.entity.state.StateEntity;
import uk.co.serin.thule.people.domain.model.state.StateCode;

@Projection(name = "summary", types = {StateEntity.class})
public interface StateProjection {
    StateCode getCode();

    String getDescription();
}
