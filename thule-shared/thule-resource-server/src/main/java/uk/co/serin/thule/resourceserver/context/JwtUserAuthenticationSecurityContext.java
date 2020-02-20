package uk.co.serin.thule.resourceserver.context;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtUserAuthenticationSecurityContext {
    private static final String JWT_USER_ID_CLAIM = "user_id";

    private DelegatingSecurityContextHolder securityContextHolder;

    public Optional<UserAuthentication> getUserAuthentication() {
        var securityContext = securityContextHolder.getContext();
        if (securityContext.getAuthentication() instanceof JwtAuthenticationToken) {
            var jwtAuthenticationToken = ((JwtAuthenticationToken) securityContext.getAuthentication());
            if (jwtAuthenticationToken.getTokenAttributes().containsKey(JWT_USER_ID_CLAIM)) {
                var userId = Long.parseLong(jwtAuthenticationToken.getTokenAttributes().get(JWT_USER_ID_CLAIM).toString());
                return Optional.of(UserAuthentication.builder().userId(userId).build());
            }
        }

        return Optional.empty();
    }
}
