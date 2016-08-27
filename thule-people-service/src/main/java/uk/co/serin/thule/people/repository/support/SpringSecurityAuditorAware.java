package uk.co.serin.thule.people.repository.support;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    private static final String CURRENT_USER_IS_NOT_AUTHENTICATED = "Current user is not authenticated";

    @Override
    public String getCurrentAuditor() {
        Assert.notNull(SecurityContextHolder.getContext().getAuthentication(), CURRENT_USER_IS_NOT_AUTHENTICATED);
        Assert.notNull(SecurityContextHolder.getContext().getAuthentication().getName(), CURRENT_USER_IS_NOT_AUTHENTICATED);

        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}