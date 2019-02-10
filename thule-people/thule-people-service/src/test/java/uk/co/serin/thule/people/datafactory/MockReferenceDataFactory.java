package uk.co.serin.thule.people.datafactory;

import org.springframework.util.StringUtils;

import uk.co.serin.thule.people.domain.entity.country.CountryEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleCode;
import uk.co.serin.thule.people.domain.entity.state.ActionEntity;
import uk.co.serin.thule.people.domain.model.state.ActionCode;
import uk.co.serin.thule.people.domain.entity.state.StateEntity;
import uk.co.serin.thule.people.domain.model.state.StateCode;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MockReferenceDataFactory implements ReferenceDataFactory {
    private final Map<ActionCode, ActionEntity> actions = new EnumMap<>(ActionCode.class);
    private final Map<String, CountryEntity> countries = new HashMap<>();
    private final Map<RoleCode, RoleEntity> roles = new EnumMap<>(RoleCode.class);
    private final Map<StateCode, StateEntity> states = new EnumMap<>(StateCode.class);

    public MockReferenceDataFactory() {
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
        var action = ActionEntity.builder().code(ActionCode.ADDRESS_DISABLE).description("Disable").build();
        actions.put(action.getCode(), action);
    }

    private void addActionAddressDiscard() {
        var action = ActionEntity.builder().code(ActionCode.ADDRESS_DISCARD).description("Discard").build();
        actions.put(ActionCode.ADDRESS_DISCARD, action);
    }

    private void addActionAddressEnable() {
        var action = ActionEntity.builder().code(ActionCode.ADDRESS_ENABLE).description("Enable").build();
        actions.put(action.getCode(), action);
    }

    private void addActionAddressRecover() {
        var action = ActionEntity.builder().code(ActionCode.ADDRESS_RECOVER).description("Recover").build();
        actions.put(action.getCode(), action);
    }

    private void addActionAddressUpdate() {
        var action = ActionEntity.builder().code(ActionCode.ADDRESS_UPDATE).description("Update").build();
        actions.put(action.getCode(), action);
    }

    private void addActionAddressView() {
        var action = ActionEntity.builder().code(ActionCode.ADDRESS_VIEW).description("View").build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonDisable() {
        var action = ActionEntity.builder().code(ActionCode.PERSON_DISABLE).description("Disable").build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonDiscard() {
        var action = ActionEntity.builder().code(ActionCode.PERSON_DISCARD).description("Discard").build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonEnable() {
        var action = ActionEntity.builder().code(ActionCode.PERSON_ENABLE).description("Enable").build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonRecover() {
        var action = ActionEntity.builder().code(ActionCode.PERSON_RECOVER).description("Recover").build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonUpdate() {
        var action = ActionEntity.builder().code(ActionCode.PERSON_UPDATE).description("Update").build();
        actions.put(action.getCode(), action);
    }

    private void addActionPersonView() {
        var action = ActionEntity.builder().code(ActionCode.PERSON_VIEW).description("View").build();
        actions.put(action.getCode(), action);
    }

    private void addCountryGb() {
        var country = CountryEntity.builder().isoCodeThreeCharacters(CountryEntity.GBR).isoCodeTwoCharacters("GB").isoName("United Kingdom").isoNumber("826").build();
        countries.put(country.getIsoCodeThreeCharacters(), country);
    }

    private void addRoleAdministrator() {
        var role = RoleEntity.builder().code(RoleCode.ROLE_ADMINISTRATOR)
                             .description(StringUtils.capitalize(RoleCode.ROLE_ADMINISTRATOR.name().toLowerCase(Locale.getDefault()))).build();
        roles.put(role.getCode(), role);
    }

    private void addRoleClerk() {
        var role = RoleEntity.builder().code(RoleCode.ROLE_CLERK).description(StringUtils.capitalize(RoleCode.ROLE_CLERK.name().toLowerCase(Locale.getDefault())))
                             .build();
        roles.put(role.getCode(), role);
    }

    private void addRoleManager() {
        var role = RoleEntity.builder().code(RoleCode.ROLE_MANAGER)
                             .description(StringUtils.capitalize(RoleCode.ROLE_MANAGER.name().toLowerCase(Locale.getDefault()))).build();
        roles.put(role.getCode(), role);
    }

    private void addStateAddressDisabled() {
        var state = StateEntity.builder().code(StateCode.ADDRESS_DISABLED)
                               .actions(Stream.of(actions.get(ActionCode.ADDRESS_ENABLE), actions.get(ActionCode.ADDRESS_VIEW)).collect(Collectors.toSet()))
                               .description("Disabled").build();
        states.put(state.getCode(), state);
    }

    private void addStateAddressDiscarded() {
        var state = StateEntity.builder().code(StateCode.ADDRESS_DISCARDED)
                               .actions(Stream.of(actions.get(ActionCode.ADDRESS_RECOVER), actions.get(ActionCode.ADDRESS_VIEW)).collect(Collectors.toSet()))
                               .description("Discarded").build();
        states.put(state.getCode(), state);
    }

    private void addStateAddressEnabled() {
        var state = StateEntity.builder().code(StateCode.ADDRESS_ENABLED).actions(
                Stream.of(actions.get(ActionCode.ADDRESS_DISABLE), actions.get(ActionCode.ADDRESS_DISCARD), actions.get(ActionCode.ADDRESS_UPDATE),
                        actions.get(ActionCode.ADDRESS_VIEW)).collect(Collectors.toSet())).description("Enabled").build();
        states.put(state.getCode(), state);
    }

    private void addStatePersonDisabled() {
        var state = StateEntity.builder().code(StateCode.PERSON_DISABLED)
                               .actions(Stream.of(actions.get(ActionCode.PERSON_ENABLE), actions.get(ActionCode.PERSON_VIEW)).collect(Collectors.toSet()))
                               .description("Disabled").build();
        states.put(state.getCode(), state);
    }

    private void addStatePersonDiscarded() {
        var state = StateEntity.builder().code(StateCode.PERSON_DISCARDED)
                               .actions(Stream.of(actions.get(ActionCode.PERSON_RECOVER), actions.get(ActionCode.PERSON_VIEW)).collect(Collectors.toSet()))
                               .description("Discarded").build();
        states.put(state.getCode(), state);
    }

    private void addStatePersonEnabled() {
        var state = StateEntity.builder().code(StateCode.PERSON_ENABLED).actions(
                Stream.of(actions.get(ActionCode.PERSON_DISABLE), actions.get(ActionCode.PERSON_DISCARD), actions.get(ActionCode.PERSON_UPDATE),
                        actions.get(ActionCode.PERSON_VIEW)).collect(Collectors.toSet())).description("Enabled").build();
        states.put(state.getCode(), state);
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
