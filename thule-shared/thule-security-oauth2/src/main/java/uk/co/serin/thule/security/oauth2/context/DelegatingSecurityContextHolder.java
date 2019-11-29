package uk.co.serin.thule.security.oauth2.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

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
        if (securityContext.getAuthentication() instanceof OAuth2Authentication) {
            var userAuthentication = ((OAuth2Authentication) securityContext.getAuthentication()).getUserAuthentication();
            if (userAuthentication.getDetails() instanceof UserAuthenticationDetails) {
                return Optional.of((UserAuthenticationDetails) userAuthentication.getDetails());
            }
        }
        return Optional.empty();
    }
}
