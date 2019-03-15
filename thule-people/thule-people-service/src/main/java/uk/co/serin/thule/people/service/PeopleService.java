package uk.co.serin.thule.people.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.model.state.ActionCode;
import uk.co.serin.thule.utils.service.trace.TracePublicMethods;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
@TracePublicMethods
@Transactional
@Validated
public class PeopleService {
    public void disable(PersonEntity personEntity) {
        updateStateWithNextState(personEntity, ActionCode.PERSON_DISABLE);
    }

    private void updateStateWithNextState(PersonEntity personEntity, ActionCode actionCode) {
        var actionToApply = personEntity.getState().getActions().stream().filter(actionEntity -> actionEntity.getCode() == actionCode).findFirst()
                                        .orElseThrow(() -> new PersonInvalidStateException(personEntity));

        personEntity.setState(actionToApply.getNextState());
    }

    public void discard(PersonEntity personEntity) {
        updateStateWithNextState(personEntity, ActionCode.PERSON_DISCARD);
    }

    public void enable(PersonEntity personEntity) {
        updateStateWithNextState(personEntity, ActionCode.PERSON_ENABLE);
    }

    public boolean isExpired(PersonEntity personEntity) {
        return LocalDate.now().isAfter(personEntity.getDateOfExpiry());
    }

    public boolean isPasswordExpired(PersonEntity personEntity) {
        return LocalDate.now().isAfter(personEntity.getDateOfPasswordExpiry());
    }

    public void recover(PersonEntity personEntity) {
        updateStateWithNextState(personEntity, ActionCode.PERSON_RECOVER);
    }
}
