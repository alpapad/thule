package uk.co.serin.thule.people.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.people.domain.email.Email;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PersonRepositoryEventHandlerTest {
    @Mock
    private EmailServiceClientAsync emailServiceClientAsync;
    @InjectMocks
    private PersonRepositoryEventHandler personRepositoryEventHandler;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private StateRepository stateRepository;

    @Test
    public void when_after_create_then_an_email_notification_is_sent() {
        // When
        personRepositoryEventHandler.afterCreate(Person.builder().userId("userId").build());

        // Then
        var emailCaptor = ArgumentCaptor.forClass(Email.class);

        verify(emailServiceClientAsync).sendEmail(emailCaptor.capture());
        var capturedEmail = emailCaptor.getValue();

        assertThat(capturedEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_after_delete_then_an_email_notification_is_sent() {
        // When
        personRepositoryEventHandler.afterDelete(Person.builder().userId("userId").build());

        // Then
        var emailCaptor = ArgumentCaptor.forClass(Email.class);

        verify(emailServiceClientAsync).sendEmail(emailCaptor.capture());
        var capturedEmail = emailCaptor.getValue();

        assertThat(capturedEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_after_save_then_an_email_notification_is_sent() {
        // When
        personRepositoryEventHandler.afterSave(Person.builder().userId("userId").build());

        // Then
        var emailCaptor = ArgumentCaptor.forClass(Email.class);

        verify(emailServiceClientAsync).sendEmail(emailCaptor.capture());
        var capturedEmail = emailCaptor.getValue();

        assertThat(capturedEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_before_create_then_roles_and_state_are_set() {
        // Given
        given(roleRepository.findByCode(any(RoleCode.class))).willReturn(Role.builder().code(RoleCode.ROLE_CLERK).build());
        given(stateRepository.findByCode(any(StateCode.class))).willReturn(State.builder().code(StateCode.PERSON_ENABLED).build());

        // When
        personRepositoryEventHandler.beforeCreate(Person.builder().userId("userId").build());

        // Then
        verify(roleRepository).findByCode(any());
        verify(stateRepository).findByCode(any());
    }
}