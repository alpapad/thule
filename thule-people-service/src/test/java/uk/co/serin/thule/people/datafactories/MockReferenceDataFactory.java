package uk.co.serin.thule.people.datafactories;

import org.springframework.util.StringUtils;

import uk.co.serin.thule.core.utils.RandomGenerators;
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

    private void addActionAddressDisable() {
        Action action = new Action(ActionCode.ADDRESS_DISABLE);
        action.setDescription("Disable");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionAddressDiscard() {
        Action action = new Action(ActionCode.ADDRESS_DISCARD);
        action.setDescription("Discard");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(ActionCode.ADDRESS_DISCARD, action);
    }

    private void addActionAddressEnable() {
        Action action = new Action(ActionCode.ADDRESS_ENABLE);
        action.setDescription("Enable");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionAddressRecover() {
        Action action = new Action(ActionCode.ADDRESS_RECOVER);
        action.setDescription("Recover");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionAddressUpdate() {
        Action action = new Action(ActionCode.ADDRESS_UPDATE);
        action.setDescription("Update");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionAddressView() {
        Action action = new Action(ActionCode.ADDRESS_VIEW);
        action.setDescription("View");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonDisable() {
        Action action = new Action(ActionCode.PERSON_DISABLE);
        action.setDescription("Disable");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonDiscard() {
        Action action = new Action(ActionCode.PERSON_DISCARD);
        action.setDescription("Discard");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonEnable() {
        Action action = new Action(ActionCode.PERSON_ENABLE);
        action.setDescription("Enable");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonRecover() {
        Action action = new Action(ActionCode.PERSON_RECOVER);
        action.setDescription("Recover");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonUpdate() {
        Action action = new Action(ActionCode.PERSON_UPDATE);
        action.setDescription("Update");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
    }

    private void addActionPersonView() {
        Action action = new Action(ActionCode.PERSON_VIEW);
        action.setDescription("View");
        action.setId(RandomGenerators.generateUniqueRandomLong());
        action.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        actions.put(action.getCode(), action);
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

    private void addCountryGb() {
        Country country = new Country(Country.GBR);
        country.setId(RandomGenerators.generateUniqueRandomLong());
        country.setIsoCodeTwoDigit("GB");
        country.setIsoName("United Kingdom");
        country.setIsoNumber("826");
        country.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        countries.put(country.getIsoCodeThreeDigit(), country);
    }

    private void addRoleAdministrator() {
        Role role = new Role(RoleCode.ROLE_ADMINISTRATOR);
        role.setDescription(StringUtils.capitalize(RoleCode.ROLE_ADMINISTRATOR.name().toLowerCase(Locale.getDefault())));
        role.setId(RandomGenerators.generateUniqueRandomLong());
        role.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        roles.put(role.getCode(), role);
    }

    private void addRoleClerk() {
        Role role = new Role(RoleCode.ROLE_CLERK);
        role.setDescription(StringUtils.capitalize(RoleCode.ROLE_CLERK.name().toLowerCase(Locale.getDefault())));
        role.setId(RandomGenerators.generateUniqueRandomLong());
        role.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        roles.put(role.getCode(), role);
    }

    private void addRoleManager() {
        Role role = new Role(RoleCode.ROLE_MANAGER);
        role.setDescription(StringUtils.capitalize(RoleCode.ROLE_MANAGER.name().toLowerCase(Locale.getDefault())));
        role.setId(RandomGenerators.generateUniqueRandomLong());
        role.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        roles.put(role.getCode(), role);
    }

    private void addRoles() {
        addRoleAdministrator();
        addRoleClerk();
        addRoleManager();
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

    private void addStateAddressDisabled() {
        State state = new State(StateCode.ADDRESS_DISABLED);
        state.addActions(Stream.of(actions.get(ActionCode.ADDRESS_ENABLE), actions.get(ActionCode.ADDRESS_VIEW)));
        state.setDescription("Disabled");
        state.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        state.setId(RandomGenerators.generateUniqueRandomLong());
        states.put(state.getCode(), state);
    }

    private void addStateAddressDiscarded() {
        State state = new State(StateCode.ADDRESS_DISCARDED);
        state.addActions(Stream.of(actions.get(ActionCode.ADDRESS_RECOVER), actions.get(ActionCode.ADDRESS_VIEW)));
        state.setDescription("Discarded");
        state.setId(RandomGenerators.generateUniqueRandomLong());
        state.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }

    private void addStateAddressEnabled() {
        State state = new State(StateCode.ADDRESS_ENABLED);
        state.addActions(Stream.of(actions.get(ActionCode.ADDRESS_DISABLE), actions.get(ActionCode.ADDRESS_DISCARD), actions.get(ActionCode.ADDRESS_UPDATE), actions.get(ActionCode.ADDRESS_VIEW)));
        state.setDescription("Enabled");
        state.setId(RandomGenerators.generateUniqueRandomLong());
        state.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }

    private void addStatePersonDisabled() {
        State state = new State(StateCode.PERSON_DISABLED);
        state.addActions(Stream.of(actions.get(ActionCode.PERSON_ENABLE), actions.get(ActionCode.PERSON_VIEW)));
        state.setDescription("Disabled");
        state.setId(RandomGenerators.generateUniqueRandomLong());
        state.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }

    private void addStatePersonDiscarded() {
        State state = new State(StateCode.PERSON_DISCARDED);
        state.addActions(Stream.of(actions.get(ActionCode.PERSON_RECOVER), actions.get(ActionCode.PERSON_VIEW)));
        state.setDescription("Discarded");
        state.setId(RandomGenerators.generateUniqueRandomLong());
        state.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }

    private void addStatePersonEnabled() {
        State state = new State(StateCode.PERSON_ENABLED);
        state.addActions(Stream.of(actions.get(ActionCode.PERSON_DISABLE), actions.get(ActionCode.PERSON_DISCARD), actions.get(ActionCode.PERSON_UPDATE), actions.get(ActionCode.PERSON_VIEW)));
        state.setDescription("Enabled");
        state.setId(RandomGenerators.generateUniqueRandomLong());
        state.getAudit().setUpdatedBy(MockReferenceDataFactory.class.getSimpleName());
        states.put(state.getCode(), state);
    }

    private void addStates() {
        addStateAddressDisabled();
        addStateAddressDiscarded();
        addStateAddressEnabled();
        addStatePersonDisabled();
        addStatePersonDiscarded();
        addStatePersonEnabled();
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
