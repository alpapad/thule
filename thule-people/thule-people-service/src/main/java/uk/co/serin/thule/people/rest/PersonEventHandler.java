package uk.co.serin.thule.people.rest;

import com.gohenry.utils.aop.TracePublicMethods;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.service.PeopleService;

@Component
@RepositoryEventHandler
@TracePublicMethods
public class PersonEventHandler {
    private PeopleService peopleService;

    public PersonEventHandler(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @HandleAfterCreate
    public void handlePersonAfterCreate(Person person) {
        peopleService.afterCreate(person);
    }

    @HandleAfterDelete
    public void handlePersonAfterDelete(Person person) {
        peopleService.afterDelete(person);
    }

    @HandleAfterSave
    public void handlePersonAfterSave(Person person) {
        peopleService.afterSave(person);
    }

    @HandleBeforeCreate
    public void handlePersonBeforeCreate(Person person) {
        peopleService.beforeCreate(person);
    }
}
