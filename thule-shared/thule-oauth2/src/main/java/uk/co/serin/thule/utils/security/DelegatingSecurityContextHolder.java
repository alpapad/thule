package uk.co.serin.thule.utils.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class DelegatingSecurityContextHolder {

    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

    public SecurityContext createEmptyContext() {
        return SecurityContextHolder.createEmptyContext();
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

    public void setContext(SecurityContext context) {
        SecurityContextHolder.setContext(context);
    }
}
