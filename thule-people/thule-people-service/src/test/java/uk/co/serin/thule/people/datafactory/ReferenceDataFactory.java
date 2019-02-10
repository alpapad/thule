package uk.co.serin.thule.people.datafactory;

import uk.co.serin.thule.people.domain.entity.country.CountryEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleCode;
import uk.co.serin.thule.people.domain.entity.state.ActionEntity;
import uk.co.serin.thule.people.domain.model.state.ActionCode;
import uk.co.serin.thule.people.domain.entity.state.StateEntity;
import uk.co.serin.thule.people.domain.model.state.StateCode;

import java.util.Map;

public interface ReferenceDataFactory {
    Map<ActionCode, ActionEntity> getActions();

    Map<String, CountryEntity> getCountries();

    Map<RoleCode, RoleEntity> getRoles();

    Map<StateCode, StateEntity> getStates();
}
