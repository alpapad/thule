package uk.co.serin.thule.email.service;

import org.springframework.core.NestedRuntimeException;


public class EmailServiceException extends NestedRuntimeException {
    private static final long serialVersionUID = 5671447707765453286L;

    public EmailServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
