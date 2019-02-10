package uk.co.serin.thule.people.domain.entity.person;

public class PersonInvalidStateException extends RuntimeException {
    private static final long serialVersionUID = 8231033769149906389L;

    public PersonInvalidStateException(PersonEntity personEntity) {
        super(personEntity.toString());
    }
}
