package uk.co.serin.thule.people.datafactories;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;

import uk.co.serin.thule.core.utils.RandomGenerators;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class MockReferenceDataFactory implements ReferenceDataFactory {
    private final Map<ActionCode, Action> actions = new EnumMap<>(ActionCode.class);
    private final Map<String, Country> countries = new HashMap<>();
    private final Map<RoleCode, Role> roles = new EnumMap<>(RoleCode.class);
    private final Map<StateCode, State> states = new EnumMap<>(StateCode.class);

    public MockReferenceDataFactory() {
        addActions();
        addCountries();
        addRoles();
        addStates();
        addStateActions();
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

    private void addActions() {
        addActionAddressDisable();
        addActionAddressDiscard();
        addActionAddressEnable();
        addActionAddressRecover();
        addActionAddressUpdate();
        addActionAddressView();
        addActionPersonDisable();
        addActionPersonDiscard();
        addActionPersonEnable();
        addActionPersonRecover();
        addActionPersonUpdate();
        addActionPersonView();
    }

    private void addCountries() {
        addCountryGb();
    }

    private void addRoles() {
        addRoleAdministrator();
        addRoleClerk();
        addRoleManager();
    }

    private void addStates() {
        addStateAddressDisabled();
        addStateAddressDiscarded();
        addStateAddressEnabled();
        addStatePersonDisabled();
        addStatePersonDiscarded();
        addStatePersonEnabled();
    }

    private void addStateActions() {
        actions.get(ActionCode.ADDRESS_DISABLE).setNextState(states.get(StateCode.ADDRESS_DISABLED));
        actions.get(ActionCode.ADDRESS_DISCARD).setNextState(states.get(StateCode.ADDRESS_DISCARDED));
        actions.get(ActionCode.ADDRESS_ENABLE).setNextState(states.get(StateCode.ADDRESS_ENABLED));
        actions.get(ActionCode.ADDRESS_RECOVER).setNextState(states.get(StateCode.ADDRESS_ENABLED));
        actions.get(ActionCode.PERSON_DISABLE).setNextState(states.get(StateCode.PERSON_DISABLED));
        actions.get(ActionCode.PERSON_DISCARD).setNextState(states.get(StateCode.PERSON_DISCARDED));
        actions.get(ActionCode.PERSON_ENABLE).setNextState(states.get(StateCode.PERSON_ENABLED));
        actions.get(ActionCode.PERSON_RECOVER).setNextState(states.get(StateCode.PERSON_ENABLED));
    }

    private void addActionAddressDisable() {
        Action action = new Action(ActionCode.ADDRESS_DISABLE).setDescription("Disable");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionAddressDiscard() {
        Action action = new Action(ActionCode.ADDRESS_DISCARD).setDescription("Discard");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(ActionCode.ADDRESS_DISCARD, action);
    }

    private void addActionAddressEnable() {
        Action action = new Action(ActionCode.ADDRESS_ENABLE).setDescription("Enable");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionAddressRecover() {
        Action action = new Action(ActionCode.ADDRESS_RECOVER).setDescription("Recover");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionAddressUpdate() {
        Action action = new Action(ActionCode.ADDRESS_UPDATE).setDescription("Update");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionAddressView() {
        Action action = new Action(ActionCode.ADDRESS_VIEW).setDescription("View");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonDisable() {
        Action action = new Action(ActionCode.PERSON_DISABLE).setDescription("Disable");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonDiscard() {
        Action action = new Action(ActionCode.PERSON_DISCARD).setDescription("Discard");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonEnable() {
        Action action = new Action(ActionCode.PERSON_ENABLE).setDescription("Enable");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonRecover() {
        Action action = new Action(ActionCode.PERSON_RECOVER).setDescription("Recover");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonUpdate() {
        Action action = new Action(ActionCode.PERSON_UPDATE).setDescription("Update");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonView() {
        Action action = new Action(ActionCode.PERSON_VIEW).setDescription("View");
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(action, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addCountryGb() {
        Country country = new Country(Country.GBR).setIsoCodeTwoDigit("GB").setIsoName("United Kingdom").setIsoNumber("826");
        ReflectionTestUtils.setField(country, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(country, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        countries.put(country.getIsoCodeThreeDigit(), country);
    }

    private void addRoleAdministrator() {
        Role role = new Role(RoleCode.ROLE_ADMINISTRATOR).
                setDescription(StringUtils.capitalize(RoleCode.ROLE_ADMINISTRATOR.name().toLowerCase(Locale.getDefault())));
        ReflectionTestUtils.setField(role, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(role, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        roles.put(role.getCode(), role);
    }

    private void addRoleClerk() {
        Role role = new Role(RoleCode.ROLE_CLERK).
                setDescription(StringUtils.capitalize(RoleCode.ROLE_CLERK.name().toLowerCase(Locale.getDefault())));
        ReflectionTestUtils.setField(role, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(role, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        roles.put(role.getCode(), role);
    }

    private void addRoleManager() {
        Role role = new Role(RoleCode.ROLE_MANAGER).
                setDescription(StringUtils.capitalize(RoleCode.ROLE_MANAGER.name().toLowerCase(Locale.getDefault())));
        ReflectionTestUtils.setField(role, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(role, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        roles.put(role.getCode(), role);
    }

    private void addStateAddressDisabled() {
        State state = new State(StateCode.ADDRESS_DISABLED).
                addActions(Stream.of(actions.get(ActionCode.ADDRESS_ENABLE), actions.get(ActionCode.ADDRESS_VIEW))).
                setDescription("Disabled");
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }

    private void addStateAddressDiscarded() {
        State state = new State(StateCode.ADDRESS_DISCARDED).
                addActions(Stream.of(actions.get(ActionCode.ADDRESS_RECOVER), actions.get(ActionCode.ADDRESS_VIEW))).
                setDescription("Discarded");
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }

    private void addStateAddressEnabled() {
        State state = new State(StateCode.ADDRESS_ENABLED).
                addActions(Stream.of(actions.get(ActionCode.ADDRESS_DISABLE), actions.get(ActionCode.ADDRESS_DISCARD), actions.get(ActionCode.ADDRESS_UPDATE), actions.get(ActionCode.ADDRESS_VIEW))).
                setDescription("Enabled");
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }

    private void addStatePersonDisabled() {
        State state = new State(StateCode.PERSON_DISABLED).
                addActions(Stream.of(actions.get(ActionCode.PERSON_ENABLE), actions.get(ActionCode.PERSON_VIEW))).
                setDescription("Disabled");
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }

    private void addStatePersonDiscarded() {
        State state = new State(StateCode.PERSON_DISCARDED).
                addActions(Stream.of(actions.get(ActionCode.PERSON_RECOVER), actions.get(ActionCode.PERSON_VIEW))).
                setDescription("Discarded");
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }

    private void addStatePersonEnabled() {
        State state = new State(StateCode.PERSON_ENABLED).
                addActions(Stream.of(actions.get(ActionCode.PERSON_DISABLE), actions.get(ActionCode.PERSON_DISCARD), actions.get(ActionCode.PERSON_UPDATE), actions.get(ActionCode.PERSON_VIEW))).
                setDescription("Enabled");
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, RandomGenerators.generateUniqueRandomLong());
        ReflectionTestUtils.setField(state, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }
}
