package uk.co.serin.thule.people.service;


import org.springframework.stereotype.Service;

import uk.co.serin.thule.people.domain.email.Email;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.person.PersonInvalidStateException;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;
import uk.co.serin.thule.people.rest.EmailServiceClient;
import uk.co.serin.thule.utils.service.trace.TracePublicMethods;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@TracePublicMethods
public class PeopleService {
    private EmailServiceClient emailServiceClient;
    private RoleRepository roleRepository;
    private StateRepository stateRepository;

    public PeopleService(RoleRepository roleRepository, StateRepository stateRepository, EmailServiceClient emailServiceClient) {
        this.roleRepository = roleRepository;
        this.stateRepository = stateRepository;
        this.emailServiceClient = emailServiceClient;
    }

    public void afterCreate(Person person) {
        sendEmail(person, "created");
    }

    private void sendEmail(Person person, String event) {
        Email email = Email.builder().
                body(String.format("Person %s %s has been %s", person.getFirstName(), person.getLastName(), event)).
                                   subject("Thule people service notification").
                                   tos(Collections.singleton(person.getEmailAddress())).
                                   build();
        emailServiceClient.create(email);
    }

    public void afterDelete(Person person) {
        sendEmail(person, "deleted");
    }

    public void afterSave(Person person) {
        sendEmail(person, "saved");
    }

    public void beforeCreate(Person person) {
        person.setRoles(Stream.of(roleRepository.findByCode(RoleCode.ROLE_CLERK)).collect(Collectors.toSet()));
        person.setState(stateRepository.findByCode(StateCode.PERSON_ENABLED));
    }

    public void disable(Person person) {
        // Validate the action is valid for the current state
        if (!getActionsByCode(person.getState().getActions()).containsKey(ActionCode.PERSON_DISABLE)) {
            throw new PersonInvalidStateException(person);
        }

        // Set new state
        Action personViewAction = getActionsByCode(person.getState().getActions()).get(ActionCode.PERSON_DISABLE);
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
        Action personViewAction = getActionsByCode(person.getState().getActions()).get(ActionCode.PERSON_DISCARD);
        person.setState(personViewAction.getNextState());
    }

    public void enable(Person person) {
        // Validate the action is valid for the current state
        if (!getActionsByCode(person.getState().getActions()).containsKey(ActionCode.PERSON_ENABLE)) {
            throw new PersonInvalidStateException(person);
        }

        // Set new state
        Action personViewAction = getActionsByCode(person.getState().getActions()).get(ActionCode.PERSON_ENABLE);
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
        Action personViewAction = getActionsByCode(person.getState().getActions()).get(ActionCode.PERSON_RECOVER);
        person.setState(personViewAction.getNextState());
    }

    public void update(Person person) {
        // Validate the action is valid for the current state
        if (!getActionsByCode(person.getState().getActions()).containsKey(ActionCode.PERSON_UPDATE)) {
            throw new PersonInvalidStateException(person);
        }
    }
}
