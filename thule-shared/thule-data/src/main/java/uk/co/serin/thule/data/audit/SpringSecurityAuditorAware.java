package uk.co.serin.thule.data.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.util.Assert;

import uk.co.serin.thule.security.oauth2.context.DelegatingSecurityContextHolder;

import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;

    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = delegatingSecurityContextHolder.getAuthentication();
        Assert.notNull(authentication, "Authentication is null");

        var principalName = authentication.getName();
        Assert.hasText(principalName, "Principal name is empty");

        return Optional.of(principalName);
    }
}