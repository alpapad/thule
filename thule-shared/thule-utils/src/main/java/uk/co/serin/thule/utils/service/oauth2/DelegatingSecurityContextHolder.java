package uk.co.serin.thule.utils.service.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import uk.co.serin.thule.utils.oauth2.UserAuthenticationDetails;

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

    public void setContext(SecurityContext context) {
        SecurityContextHolder.setContext(context);
    }

    public UserAuthenticationDetails getUserAuthenticationDetails() {
        var securityContext = SecurityContextHolder.getContext();
        if (securityContext.getAuthentication() instanceof OAuth2Authentication) {
            var oAuth2Authentication = (OAuth2Authentication) securityContext.getAuthentication();
            var details = oAuth2Authentication.getUserAuthentication().getDetails();
            if (details instanceof UserAuthenticationDetails) {
                return (UserAuthenticationDetails) details;
            } else {
                throw new SecurityException(String.format("OAuth2 user authentication details are invalid [%s]", details));
            }

        } else {
            throw new SecurityException(String.format("Security context authentication is the wrong type, expecting [%s] but was [%s]",
                    OAuth2Authentication.class.getSimpleName(), securityContext.getAuthentication().getClass().getSimpleName()));
        }
    }
}
