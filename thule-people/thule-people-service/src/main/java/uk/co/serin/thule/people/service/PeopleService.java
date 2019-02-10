package uk.co.serin.thule.people.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.entity.person.PersonInvalidStateException;
import uk.co.serin.thule.people.domain.entity.state.ActionEntity;
import uk.co.serin.thule.people.domain.model.state.ActionCode;
import uk.co.serin.thule.people.service.email.EmailServiceClient;
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

    public void disable(PersonEntity personEntity) {
        // Validate the action is valid for the current nextState
        if (!getActionsByCode(personEntity.getState().getActions()).containsKey(ActionCode.PERSON_DISABLE)) {
            throw new PersonInvalidStateException(personEntity);
        }

        // Set new nextState
        var personViewAction = getActionsByCode(personEntity.getState().getActions()).get(ActionCode.PERSON_DISABLE);
        personEntity.setState(personViewAction.getNextState());
    }

    private Map<ActionCode, ActionEntity> getActionsByCode(Set<ActionEntity> actionEntities) {
        return actionEntities.stream().collect(Collectors.toMap(ActionEntity::getCode, Function.identity()));
    }

    public void discard(PersonEntity personEntity) {
        // Validate the action is valid for the current nextState
        if (!getActionsByCode(personEntity.getState().getActions()).containsKey(ActionCode.PERSON_DISCARD)) {
            throw new PersonInvalidStateException(personEntity);
        }

        // Set new nextState
        var personViewAction = getActionsByCode(personEntity.getState().getActions()).get(ActionCode.PERSON_DISCARD);
        personEntity.setState(personViewAction.getNextState());
    }

    public void enable(PersonEntity personEntity) {
        // Validate the action is valid for the current nextState
        if (!getActionsByCode(personEntity.getState().getActions()).containsKey(ActionCode.PERSON_ENABLE)) {
            throw new PersonInvalidStateException(personEntity);
        }

        // Set new nextState
        var personViewAction = getActionsByCode(personEntity.getState().getActions()).get(ActionCode.PERSON_ENABLE);
        personEntity.setState(personViewAction.getNextState());
    }

    public boolean isExpired(PersonEntity personEntity) {
        return LocalDate.now().isAfter(personEntity.getDateOfExpiry());
    }

    public boolean isPasswordExpired(PersonEntity personEntity) {
        return LocalDate.now().isAfter(personEntity.getDateOfPasswordExpiry());
    }

    public void recover(PersonEntity personEntity) {
        // Validate the action is valid for the current nextState
        if (!getActionsByCode(personEntity.getState().getActions()).containsKey(ActionCode.PERSON_RECOVER)) {
            throw new PersonInvalidStateException(personEntity);
        }

        // Set new nextState
        var personViewAction = getActionsByCode(personEntity.getState().getActions()).get(ActionCode.PERSON_RECOVER);
        personEntity.setState(personViewAction.getNextState());
    }

    public void update(PersonEntity personEntity) {
        // Validate the action is valid for the current nextState
        if (!getActionsByCode(personEntity.getState().getActions()).containsKey(ActionCode.PERSON_UPDATE)) {
            throw new PersonInvalidStateException(personEntity);
        }
    }
}
