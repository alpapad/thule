package uk.co.serin.thule.people.service;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import uk.co.serin.thule.people.rest.EmailServiceClient;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PeopleServiceTest {
    @Mock
    private EmailServiceClient emailServiceClient;
    @InjectMocks
    private PeopleService peopleService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private StateRepository stateRepository;

    @Test
    public void when_after_create_then_email_notification_is_sent() {
        // Given
        Set<Email> actualEmails = new HashSet<>();
        given(emailServiceClient.create(any(Email.class))).willAnswer(invocation -> {
            Email actualEmail = (Email) invocation.getArguments()[0];
            actualEmails.add(actualEmail);
            return actualEmail;
        });

        // When
        peopleService.afterCreate(new Person("userId"));

        // Then
        Optional<Email> actualEmailOptional = actualEmails.stream().findFirst();
        Email actualEmail = actualEmailOptional.orElseThrow();
        assertThat(actualEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_after_delete_then_email_notification_is_sent() {
        // Given
        Set<Email> actualEmails = new HashSet<>();
        given(emailServiceClient.create(any(Email.class))).willAnswer(invocation -> {
            Email actualEmail = (Email) invocation.getArguments()[0];
            actualEmails.add(actualEmail);
            return actualEmail;
        });

        // When
        peopleService.afterDelete(new Person("userId"));

        // Then
        Optional<Email> actualEmailOptional = actualEmails.stream().findFirst();
        Email actualEmail = actualEmailOptional.orElseThrow();
        assertThat(actualEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_after_save_then_email_notification_is_sent() {
        // Given
        Set<Email> actualEmails = new HashSet<>();
        given(emailServiceClient.create(any(Email.class))).willAnswer(invocation -> {
            Email actualEmail = (Email) invocation.getArguments()[0];
            actualEmails.add(actualEmail);
            return actualEmail;
        });

        // When
        peopleService.afterSave(new Person("userId"));

        // Then
        Optional<Email> actualEmailOptional = actualEmails.stream().findFirst();
        Email actualEmail = actualEmailOptional.orElseThrow();
        assertThat(actualEmail.getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void when_before_create_then_roles_and_state_are_set() {
        // Given
        given(roleRepository.findByCode(any(RoleCode.class))).willReturn(new Role(RoleCode.ROLE_CLERK));
        given(stateRepository.findByCode(any(StateCode.class))).willReturn(new State(StateCode.PERSON_ENABLED));

        // When
        peopleService.beforeCreate(new Person("userId"));

        // Then
        verify(roleRepository).findByCode(any());
        verify(stateRepository).findByCode(any());
    }
}