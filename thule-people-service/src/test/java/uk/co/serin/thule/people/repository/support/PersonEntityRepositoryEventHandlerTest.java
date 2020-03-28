package uk.co.serin.thule.people.repository.support;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleCode;
import uk.co.serin.thule.people.domain.entity.role.RoleEntity;
import uk.co.serin.thule.people.domain.entity.state.StateEntity;
import uk.co.serin.thule.people.domain.model.email.Email;
import uk.co.serin.thule.people.domain.model.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;
import uk.co.serin.thule.people.service.email.EmailServiceClientAsync;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PersonEntityRepositoryEventHandlerTest {
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
        personRepositoryEventHandler.afterCreate(PersonEntity.builder().emailAddress("test@test.com").userId("userId").build());

        // Then
        var emailCaptor = ArgumentCaptor.forClass(Email.class);

        verify(emailServiceClientAsync).sendEmail(emailCaptor.capture());
        var capturedEmail = emailCaptor.getValue();

        assertThat(capturedEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_after_delete_then_an_email_notification_is_sent() {
        // When
        personRepositoryEventHandler.afterDelete(PersonEntity.builder().emailAddress("test@test.com").userId("userId").build());

        // Then
        var emailCaptor = ArgumentCaptor.forClass(Email.class);

        verify(emailServiceClientAsync).sendEmail(emailCaptor.capture());
        var capturedEmail = emailCaptor.getValue();

        assertThat(capturedEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_after_save_then_an_email_notification_is_sent() {
        // When
        personRepositoryEventHandler.afterSave(PersonEntity.builder().emailAddress("test@test.com").userId("userId").build());

        // Then
        var emailCaptor = ArgumentCaptor.forClass(Email.class);

        verify(emailServiceClientAsync).sendEmail(emailCaptor.capture());
        var capturedEmail = emailCaptor.getValue();

        assertThat(capturedEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_before_create_then_roles_and_state_are_set() {
        // Given
        given(roleRepository.findByCode(any(RoleCode.class))).willReturn(RoleEntity.builder().code(RoleCode.ROLE_CLERK).build());
        given(stateRepository.findByCode(any(StateCode.class))).willReturn(StateEntity.builder().code(StateCode.PERSON_ENABLED).build());

        // When
        personRepositoryEventHandler.beforeCreate(PersonEntity.builder().userId("userId").build());

        // Then
        verify(roleRepository).findByCode(any());
        verify(stateRepository).findByCode(any());
    }
}