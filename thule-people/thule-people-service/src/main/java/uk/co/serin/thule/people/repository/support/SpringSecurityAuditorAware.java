package uk.co.serin.thule.people.repository.support;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    public Optional<String> getCurrentAuditor() {
        Assert.notNull(SecurityContextHolder.getContext().getAuthentication(), "Authentication is null");
        Assert.hasText(SecurityContextHolder.getContext().getAuthentication().getName(), "Authentication name is empty");

        return Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}