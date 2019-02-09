package uk.co.serin.thule.people.rest;


import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import uk.co.serin.thule.people.domain.email.Email;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;
import uk.co.serin.thule.utils.service.trace.TracePublicMethods;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@RepositoryEventHandler
@Service
@Slf4j
@TracePublicMethods
@Validated
public class PersonRepositoryEventHandler {
    private EmailServiceClientAsync emailServiceClient;
    private RoleRepository roleRepository;
    private StateRepository stateRepository;

    @HandleAfterCreate
    public void afterCreate(Person person) {
        sendEmail(person, "created");
    }

    private void sendEmail(Person person, String event) {
        var email = Email.builder().body(String.format("Person %s %s has been %s", person.getFirstName(), person.getLastName(), event))
                         .subject("Thule people service notification").tos(Collections.singleton(person.getEmailAddress())).build();
        emailServiceClient.sendEmail(email);
    }

    @HandleAfterDelete
    public void afterDelete(Person person) {
        sendEmail(person, "deleted");
    }

    @HandleAfterSave
    public void afterSave(Person person) {
        sendEmail(person, "saved");
    }

    @HandleBeforeCreate
    public void beforeCreate(Person person) {
        person.setRoles(Stream.of(roleRepository.findByCode(RoleCode.ROLE_CLERK)).collect(Collectors.toSet()));
        person.setState(stateRepository.findByCode(StateCode.PERSON_ENABLED));
    }
}
