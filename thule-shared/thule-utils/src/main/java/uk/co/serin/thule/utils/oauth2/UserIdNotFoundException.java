package uk.co.serin.thule.utils.oauth2;

import org.springframework.core.NestedRuntimeException;

public class UserIdNotFoundException extends NestedRuntimeException {
    private static final long serialVersionUID = 5671447707765453286L;

    public UserIdNotFoundException(String message) {
        super(message);
    }
}
