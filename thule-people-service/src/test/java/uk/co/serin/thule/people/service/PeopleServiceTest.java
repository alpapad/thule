package uk.co.serin.thule.people.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.email.Email;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.role.RoleCode;
import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

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
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void after_create() {
        // Given
        Set<Email> actualEmails = new HashSet<>();
        given(emailServiceClient.create(any(Email.class))).willAnswer(invocation -> {
            Email actualEmail = Email.class.cast(invocation.getArguments()[0]);
            actualEmails.add(actualEmail);
            return actualEmail;
        });

        // When
        peopleService.afterCreate(new Person("userId"));

        // Then
        Optional<Email> actualEmail = actualEmails.stream().findFirst();
        assertThat(actualEmail).isPresent();
        assertThat(actualEmail.get().getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void after_delete() {
        // Given
        Set<Email> actualEmails = new HashSet<>();
        given(emailServiceClient.create(any(Email.class))).willAnswer(invocation -> {
            Email actualEmail = Email.class.cast(invocation.getArguments()[0]);
            actualEmails.add(actualEmail);
            return actualEmail;
        });

        // When
        peopleService.afterDelete(new Person("userId"));

        // Then
        Optional<Email> actualEmail = actualEmails.stream().findFirst();
        assertThat(actualEmail).isPresent();
        assertThat(actualEmail.get().getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void after_save() {
        // Given
        Set<Email> actualEmails = new HashSet<>();
        given(emailServiceClient.create(any(Email.class))).willAnswer(invocation -> {
            Email actualEmail = Email.class.cast(invocation.getArguments()[0]);
            actualEmails.add(actualEmail);
            return actualEmail;
        });

        // When
        peopleService.afterSave(new Person("userId"));

        // Then
        Optional<Email> actualEmail = actualEmails.stream().findFirst();
        assertThat(actualEmail).isPresent();
        assertThat(actualEmail.get().getSubject()).isEqualTo("Thule people service notification");
    }

    @Test
    public void before_create() {
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