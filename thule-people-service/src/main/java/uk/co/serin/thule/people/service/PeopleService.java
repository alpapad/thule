package uk.co.serin.thule.people.service;

import org.springframework.stereotype.Service;

import uk.co.serin.thule.utils.aspects.TracePublicMethods;
import uk.co.serin.thule.people.domain.email.Email;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

import java.util.Collections;
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
        Email email = Email.EmailBuilder.anEmail().
                withBody(String.format("Person %s %s has been %s", person.getFirstName(), person.getLastName(), event)).
                withSubject("Thule people service notification").
                withTos(Collections.singleton(person.getEmailAddress())).
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
        person.addRoles(Stream.of(roleRepository.findByCode(RoleCode.ROLE_CLERK)).collect(Collectors.toSet()));
        person.setState(stateRepository.findByCode(StateCode.PERSON_ENABLED));
    }
}
