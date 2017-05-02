package uk.co.serin.thule.people.rest.core;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import uk.co.serin.thule.core.aspects.LogPublicMethods;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

import java.util.stream.Stream;

@Component
@RepositoryEventHandler
@LogPublicMethods
public class PersonEventHandler {
    private RoleRepository roleRepository;
    private StateRepository stateRepository;

    public PersonEventHandler(RoleRepository roleRepository, StateRepository stateRepository) {
        this.roleRepository = roleRepository;
        this.stateRepository = stateRepository;
    }

    @HandleBeforeCreate
    public void handlePersonCreate(Person person) {
        person.addRoles(Stream.of(roleRepository.findByCode(RoleCode.ROLE_CLERK)));
        person.setState(stateRepository.findByCode(StateCode.PERSON_ENABLED));
    }
}
