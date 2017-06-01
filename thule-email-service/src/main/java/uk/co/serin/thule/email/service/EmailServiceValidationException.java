package uk.co.serin.thule.email.service;

import org.springframework.core.NestedRuntimeException;

public class EmailServiceValidationException extends NestedRuntimeException {
    private static final long serialVersionUID = 8981124139978326447L;

    public EmailServiceValidationException(String message) {
        super(message);
    }

    public EmailServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
