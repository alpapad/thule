package uk.co.serin.thule.people.service;

import org.springframework.core.NestedRuntimeException;

public class PersonNotFoundException extends NestedRuntimeException {
    private static final long serialVersionUID = 8231033769149906389L;

    public PersonNotFoundException(String msg) {
        super(msg);
    }
}
