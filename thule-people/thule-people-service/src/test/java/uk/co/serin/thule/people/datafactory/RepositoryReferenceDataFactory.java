package uk.co.serin.thule.people.datafactory;


import uk.co.serin.thule.people.domain.entity.country.CountryEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleCode;
import uk.co.serin.thule.people.domain.entity.state.ActionEntity;
import uk.co.serin.thule.people.domain.model.state.ActionCode;
import uk.co.serin.thule.people.domain.entity.state.StateEntity;
import uk.co.serin.thule.people.domain.model.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.ActionRepository;
import uk.co.serin.thule.people.repository.repositories.CountryRepository;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RepositoryReferenceDataFactory implements ReferenceDataFactory {
    private final Map<ActionCode, ActionEntity> actions;
    private final Map<String, CountryEntity> countries;
    private final Map<RoleCode, RoleEntity> roles;
    private final Map<StateCode, StateEntity> states;

    public RepositoryReferenceDataFactory(ActionRepository actionRepository, StateRepository stateRepository, RoleRepository roleRepository, CountryRepository countryRepository) {
        actions = actionRepository.findAllWithNextState().stream().collect(Collectors.toMap(ActionEntity::getCode, Function.identity()));
        states = stateRepository.findAllWithActions().stream().collect(Collectors.toMap(StateEntity::getCode, Function.identity()));
        roles = StreamSupport.stream(roleRepository.findAll().spliterator(), false).collect(Collectors.toMap(RoleEntity::getCode, Function.identity()));
        countries = StreamSupport.stream(countryRepository.findAll().spliterator(), false).collect(Collectors.toMap(CountryEntity::getIsoCodeThreeCharacters, Function.identity()));
    }

    @Override
    public Map<ActionCode, ActionEntity> getActions() {
        return actions;
    }

    @Override
    public Map<String, CountryEntity> getCountries() {
        return countries;
    }

    @Override
    public Map<RoleCode, RoleEntity> getRoles() {
        return roles;
    }

    @Override
    public Map<StateCode, StateEntity> getStates() {
        return states;
    }
}
