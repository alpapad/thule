package uk.co.serin.thule.people.datafactory;

import org.springframework.util.StringUtils;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MockReferenceDataFactory implements ReferenceDataFactory {
    private final Map<ActionCode, Action> actions = new EnumMap<>(ActionCode.class);
    private final Map<String, Country> countries = new HashMap<>();
    private final Map<RoleCode, Role> roles = new EnumMap<>(RoleCode.class);
    private final Map<StateCode, State> states = new EnumMap<>(StateCode.class);

    MockReferenceDataFactory() {
        addActions();
        addCountries();
        addRoles();
        addStates();
        addStateActions();
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
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.ADDRESS_DISABLE).
                withDescription("Disable").
                build();
        actions.put(action.getCode(), action);
    }

    private void addActionAddressDiscard() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.ADDRESS_DISCARD).
                withDescription("Discard").
                build();
        actions.put(ActionCode.ADDRESS_DISCARD, action);
    }

    private void addActionAddressEnable() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.ADDRESS_ENABLE).
                withDescription("Enable").
                build();
        actions.put(action.getCode(), action);
    }

    private void addActionAddressRecover() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.ADDRESS_RECOVER).
                withDescription("Recover").
                build();
        actions.put(action.getCode(), action);
    }

    private void addActionAddressUpdate() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.ADDRESS_UPDATE).
                withDescription("Update").
                build();
        actions.put(action.getCode(), action);
    }

    private void addActionAddressView() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.ADDRESS_VIEW).
                withDescription("View").
                build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonDisable() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.PERSON_DISABLE).
                withDescription("Disable").
                build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonDiscard() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.PERSON_DISCARD).
                withDescription("Discard").
                build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonEnable() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.PERSON_ENABLE).
                withDescription("Enable").
                build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonRecover() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.PERSON_RECOVER).
                withDescription("Recover").
                build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonUpdate() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.PERSON_UPDATE).
                withDescription("Update").
                build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonView() {
        Action action = Action.ActionBuilder.anAction().
                withCode(ActionCode.PERSON_VIEW).
                withDescription("View").
                build();
        actions.put(action.getCode(), action);
    }

    private void addCountryGb() {
        Country country = Country.CountryBuilder.aCountry().
                withIsoCodeThreeDigit(Country.GBR).
                withIsoCodeTwoDigit("GB").
                withIsoName("United Kingdom").
                withIsoNumber("826").
                build();
        countries.put(country.getIsoCodeThreeDigit(), country);
    }

    private void addRoleAdministrator() {
        Role role = Role.RoleBuilder.aRole().
                withCode(RoleCode.ROLE_ADMINISTRATOR).
                withDescription(StringUtils.capitalize(RoleCode.ROLE_ADMINISTRATOR.name().toLowerCase(Locale.getDefault()))).
                build();
        roles.put(role.getCode(), role);
    }

    private void addRoleClerk() {
        Role role = Role.RoleBuilder.aRole().
                withCode(RoleCode.ROLE_CLERK).
                withDescription(StringUtils.capitalize(RoleCode.ROLE_CLERK.name().toLowerCase(Locale.getDefault()))).
                build();
        roles.put(role.getCode(), role);
    }

    private void addRoleManager() {
        Role role = Role.RoleBuilder.aRole().
                withCode(RoleCode.ROLE_MANAGER).
                withDescription(StringUtils.capitalize(RoleCode.ROLE_MANAGER.name().toLowerCase(Locale.getDefault()))).
                build();
        roles.put(role.getCode(), role);
    }

    private void addStateAddressDisabled() {
        State state = State.StateBuilder.aState().
                withCode(StateCode.ADDRESS_DISABLED).
                withActions(Stream.of(actions.get(ActionCode.ADDRESS_ENABLE), actions.get(ActionCode.ADDRESS_VIEW)).collect(Collectors.toSet())).
                withDescription("Disabled").
                build();
        states.put(state.getCode(), state);
    }

    private void addStateAddressDiscarded() {
        State state = State.StateBuilder.aState().
                withCode(StateCode.ADDRESS_DISCARDED).
                withActions(Stream.of(actions.get(ActionCode.ADDRESS_RECOVER), actions.get(ActionCode.ADDRESS_VIEW)).collect(Collectors.toSet())).
                withDescription("Discarded").
                build();
        states.put(state.getCode(), state);
    }

    private void addStateAddressEnabled() {
        State state = State.StateBuilder.aState().
                withCode(StateCode.ADDRESS_ENABLED).
                withActions(Stream.of(actions.get(ActionCode.ADDRESS_DISABLE), actions.get(ActionCode.ADDRESS_DISCARD), actions.get(ActionCode.ADDRESS_UPDATE), actions.get(ActionCode.ADDRESS_VIEW)).collect(Collectors.toSet())).
                withDescription("Enabled").
                build();
        states.put(state.getCode(), state);
    }

    private void addStatePersonDisabled() {
        State state = State.StateBuilder.aState().
                withCode(StateCode.PERSON_DISABLED).
                withActions(Stream.of(actions.get(ActionCode.PERSON_ENABLE), actions.get(ActionCode.PERSON_VIEW)).collect(Collectors.toSet())).
                withDescription("Disabled").
                build();
        states.put(state.getCode(), state);
    }

    private void addStatePersonDiscarded() {
        State state = State.StateBuilder.aState().
                withCode(StateCode.PERSON_DISCARDED).
                withActions(Stream.of(actions.get(ActionCode.PERSON_RECOVER), actions.get(ActionCode.PERSON_VIEW)).collect(Collectors.toSet())).
                withDescription("Discarded").
                build();
        states.put(state.getCode(), state);
    }

    private void addStatePersonEnabled() {
        State state = State.StateBuilder.aState().
                withCode(StateCode.PERSON_ENABLED).
                withActions(Stream.of(actions.get(ActionCode.PERSON_DISABLE), actions.get(ActionCode.PERSON_DISCARD), actions.get(ActionCode.PERSON_UPDATE), actions.get(ActionCode.PERSON_VIEW)).collect(Collectors.toSet())).
                withDescription("Enabled").
                build();
        states.put(state.getCode(), state);
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
