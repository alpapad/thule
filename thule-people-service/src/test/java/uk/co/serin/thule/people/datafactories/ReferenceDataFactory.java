package uk.co.serin.thule.people.datafactories;

import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;

import java.util.Map;

public interface ReferenceDataFactory {
    Map<ActionCode, Action> getActions();

    Map<String, Country> getCountries();

    Map<RoleCode, Role> getRoles();

    Map<StateCode, State> getStates();
}
