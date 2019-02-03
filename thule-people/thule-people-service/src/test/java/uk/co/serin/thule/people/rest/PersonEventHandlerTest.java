package uk.co.serin.thule.people.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.service.PeopleService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PersonEventHandlerTest {
    @Mock
    private PeopleService peopleService;
    @InjectMocks
    private PersonEventHandler personEventHandler;

    @Test
    public void when_handle_person__before_create_then_delegate_to_people_service() {
        // Given

        // When
        personEventHandler.handlePersonBeforeCreate(Person.builder().userId("userId").build());

        // Then
        verify(peopleService).beforeCreate(any(Person.class));
    }

    @Test
    public void when_handle_person_after_create_then_delegate_to_people_service() {
        // Given

        // When
        personEventHandler.handlePersonAfterCreate(Person.builder().userId("userId").build());

        // Then
        verify(peopleService).afterCreate(any(Person.class));
    }

    @Test
    public void when_handle_person_after_delete_then_delegate_to_people_service() {
        // Given

        // When
        personEventHandler.handlePersonAfterDelete(Person.builder().userId("userId").build());

        // Then
        verify(peopleService).afterDelete(any(Person.class));
    }

    @Test
    public void when_handle_person_after_save_then_delegate_to_people_service() {
        // Given

        // When
        personEventHandler.handlePersonAfterSave(Person.builder().userId("userId").build());

        // Then
        verify(peopleService).afterSave(any(Person.class));
    }
}