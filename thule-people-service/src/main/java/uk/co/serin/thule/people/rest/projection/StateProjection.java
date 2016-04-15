package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;

@Projection(name = "summary", types = {State.class})
public interface StateProjection {
    StateCode getCode();

    String getDescription();
}
