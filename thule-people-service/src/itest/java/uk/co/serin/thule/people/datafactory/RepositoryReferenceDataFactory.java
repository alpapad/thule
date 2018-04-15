package uk.co.serin.thule.people.datafactory;


import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.ActionRepository;
import uk.co.serin.thule.people.repository.repositories.CountryRepository;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RepositoryReferenceDataFactory implements ReferenceDataFactory {
    private final Map<ActionCode, Action> actions;
    private final Map<String, Country> countries;
    private final Map<RoleCode, Role> roles;
    private final Map<StateCode, State> states;

    @SuppressWarnings("unchecked")
    public RepositoryReferenceDataFactory(ActionRepository actionRepository, StateRepository stateRepository, RoleRepository roleRepository, CountryRepository countryRepository) {
        actions = actionRepository.findAllWithNextState().stream().collect(Collectors.toMap(Action::getCode, Function.identity()));
        states = stateRepository.findAllWithActions().stream().collect(Collectors.toMap(State::getCode, Function.identity()));
        roles = StreamSupport.stream(roleRepository.findAll().spliterator(), false).collect(Collectors.toMap(Role::getCode, Function.identity()));
        countries = StreamSupport.stream(countryRepository.findAll().spliterator(), false).collect(Collectors.toMap(Country::getIsoCodeThreeDigit, Function.identity()));
    }

    @Override
    public Map<ActionCode, Action> getActions() {
        return actions;
    }

    @Override
    public Map<String, Country> getCountries() {
        return countries;
    }

    @Override
    public Map<RoleCode, Role> getRoles() {
        return roles;
    }

    @Override
    public Map<StateCode, State> getStates() {
        return states;
    }
}
