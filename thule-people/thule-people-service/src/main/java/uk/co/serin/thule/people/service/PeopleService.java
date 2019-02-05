package uk.co.serin.thule.people.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.person.PersonInvalidStateException;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.rest.EmailServiceClient;
import uk.co.serin.thule.utils.service.trace.TracePublicMethods;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@AllArgsConstructor
@TracePublicMethods
@Slf4j
@Transactional
public class PeopleService {
    private EmailServiceClient emailServiceClient;

    public void disable(Person person) {
        // Validate the action is valid for the current state
        if (!getActionsByCode(person.getState().getActions()).containsKey(ActionCode.PERSON_DISABLE)) {
            throw new PersonInvalidStateException(person);
        }

        // Set new state
        var personViewAction = getActionsByCode(person.getState().getActions()).get(ActionCode.PERSON_DISABLE);
        person.setState(personViewAction.getNextState());
    }

    private Map<ActionCode, Action> getActionsByCode(Set<Action> actions) {
        return actions.stream().collect(Collectors.toMap(Action::getCode, Function.identity()));
    }

    public void discard(Person person) {
        // Validate the action is valid for the current state
        if (!getActionsByCode(person.getState().getActions()).containsKey(ActionCode.PERSON_DISCARD)) {
            throw new PersonInvalidStateException(person);
        }

        // Set new state
        var personViewAction = getActionsByCode(person.getState().getActions()).get(ActionCode.PERSON_DISCARD);
        person.setState(personViewAction.getNextState());
    }

    public void enable(Person person) {
        // Validate the action is valid for the current state
        if (!getActionsByCode(person.getState().getActions()).containsKey(ActionCode.PERSON_ENABLE)) {
            throw new PersonInvalidStateException(person);
        }

        // Set new state
        var personViewAction = getActionsByCode(person.getState().getActions()).get(ActionCode.PERSON_ENABLE);
        person.setState(personViewAction.getNextState());
    }

    public boolean isExpired(Person person) {
        return LocalDate.now().isAfter(person.getDateOfExpiry());
    }

    public boolean isPasswordExpired(Person person) {
        return LocalDate.now().isAfter(person.getDateOfPasswordExpiry());
    }

    public void recover(Person person) {
        // Validate the action is valid for the current state
        if (!getActionsByCode(person.getState().getActions()).containsKey(ActionCode.PERSON_RECOVER)) {
            throw new PersonInvalidStateException(person);
        }

        // Set new state
        var personViewAction = getActionsByCode(person.getState().getActions()).get(ActionCode.PERSON_RECOVER);
        person.setState(personViewAction.getNextState());
    }

    public void update(Person person) {
        // Validate the action is valid for the current state
        if (!getActionsByCode(person.getState().getActions()).containsKey(ActionCode.PERSON_UPDATE)) {
            throw new PersonInvalidStateException(person);
        }
    }
}
