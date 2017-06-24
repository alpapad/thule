package uk.co.serin.thule.people.rest.core;

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
    public void handle_person__before_create() {
        // Given

        // When
        personEventHandler.handlePersonBeforeCreate(new Person("userId"));

        // Then
        verify(peopleService).beforeCreate(any(Person.class));
    }

    @Test
    public void handle_person_after_create() {
        // Given

        // When
        personEventHandler.handlePersonAfterCreate(new Person("userId"));

        // Then
        verify(peopleService).afterCreate(any(Person.class));
    }

    @Test
    public void handle_person_after_delete() {
        // Given

        // When
        personEventHandler.handlePersonAfterDelete(new Person("userId"));

        // Then
        verify(peopleService).afterDelete(any(Person.class));
    }

    @Test
    public void handle_person_after_save() {
        // Given

        // When
        personEventHandler.handlePersonAfterSave(new Person("userId"));

        // Then
        verify(peopleService).afterSave(any(Person.class));
    }
}