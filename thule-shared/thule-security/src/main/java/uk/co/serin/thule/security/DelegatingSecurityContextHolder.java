package uk.co.serin.thule.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

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

    public Optional<UserAuthenticationDetails> getUserAuthenticationDetails() {
        var securityContext = SecurityContextHolder.getContext();
        var details = securityContext.getAuthentication().getDetails();
        if (details instanceof  UserAuthenticationDetails) {
            return Optional.of((UserAuthenticationDetails) details);
        } else {
            return Optional.empty();
        }
    }
}
