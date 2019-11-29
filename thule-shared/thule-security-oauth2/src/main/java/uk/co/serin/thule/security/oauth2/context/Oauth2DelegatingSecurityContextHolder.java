package uk.co.serin.thule.security.oauth2.context;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;
import uk.co.serin.thule.security.context.UserAuthenticationDetails;

import java.util.Optional;

public class Oauth2DelegatingSecurityContextHolder extends DelegatingSecurityContextHolder {

    @Override
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
