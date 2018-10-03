package uk.co.serin.thule.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

/**
 * Wrapper is used as the SecurityContextHolder class is static methods only.
 * So a wrapper is created in order to aid testing purposes.
 */
@Service
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

    public UserAuthenticationDetails getUserAuthenticationDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) securityContext.getAuthentication();
        Authentication authentication = oAuth2Authentication.getUserAuthentication();
        return (UserAuthenticationDetails) authentication.getDetails();
    }

    public void setContext(SecurityContext context) {
        SecurityContextHolder.setContext(context);
    }
}
