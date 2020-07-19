package uk.co.serin.thule.people.service;

import org.springframework.core.NestedRuntimeException;

public class PhotographNotFoundException extends NestedRuntimeException {
    private static final long serialVersionUID = 8231033769149906389L;

    public PhotographNotFoundException(String msg) {
        super(msg);
    }
}
