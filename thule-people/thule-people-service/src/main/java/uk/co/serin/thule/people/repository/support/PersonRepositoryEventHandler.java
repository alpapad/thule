package uk.co.serin.thule.people.repository.support;


import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleCode;
import uk.co.serin.thule.people.domain.model.email.Email;
import uk.co.serin.thule.people.domain.model.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;
import uk.co.serin.thule.people.service.email.EmailServiceClientAsync;
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
    public void afterCreate(PersonEntity person) {
        sendEmail(person, "created");
    }

    private void sendEmail(PersonEntity person, String event) {
        var email = Email.builder().body(String.format("PersonEntity %s %s has been %s", person.getFirstName(), person.getLastName(), event))
                         .subject("Thule people service notification").tos(Collections.singleton(person.getEmailAddress())).build();
        emailServiceClient.sendEmail(email);
    }

    @HandleAfterDelete
    public void afterDelete(PersonEntity person) {
        sendEmail(person, "deleted");
    }

    @HandleAfterSave
    public void afterSave(PersonEntity person) {
        sendEmail(person, "saved");
    }

    @HandleBeforeCreate
    public void beforeCreate(PersonEntity person) {
        person.setRoles(Stream.of(roleRepository.findByCode(RoleCode.ROLE_CLERK)).collect(Collectors.toSet()));
        person.setState(stateRepository.findByCode(StateCode.PERSON_ENABLED));
    }
}