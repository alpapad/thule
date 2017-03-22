package uk.co.serin.thule.people.rest.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PersonEventHandlerTest {
    private PersonEventHandler personEventHandler;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private StateRepository stateRepository;

    @Test
    public void handlePersonCreate() {
        // Given
        given(roleRepository.findByCode(any(RoleCode.class))).willReturn(new Role(RoleCode.ROLE_CLERK));
        given(stateRepository.findByCode(any(StateCode.class))).willReturn(new State(StateCode.PERSON_ENABLED));

        // When
        personEventHandler.handlePersonCreate(new Person("userId"));

        // Then
        verify(roleRepository).findByCode(any());
        verify(stateRepository).findByCode(any());
    }

    @Before
    public void setUp() {
        personEventHandler = new PersonEventHandler(roleRepository, stateRepository);
    }
}