package uk.co.serin.thule.people.service;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import uk.co.serin.thule.utils.service.oauth2.DelegatingSecurityContextHolder;

import java.util.Optional;

@Service
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;

    public SpringSecurityAuditorAware(DelegatingSecurityContextHolder delegatingSecurityContextHolder) {
        this.delegatingSecurityContextHolder = delegatingSecurityContextHolder;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = delegatingSecurityContextHolder.getAuthentication();
        Assert.notNull(authentication, "Authentication is null");

        var principalName = authentication.getName();
        Assert.hasText(principalName, "Principal name is empty");

        return Optional.of(principalName);
    }
}

