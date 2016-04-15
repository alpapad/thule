package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;

@Projection(name = "summary", types = {Action.class})
public interface ActionProjection {
    ActionCode getCode();

    String getDescription();
}
