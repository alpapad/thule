package uk.co.serin.thule.utils.service.data.auditor;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import uk.co.serin.thule.utils.service.oauth2.DelegatingSecurityContextHolder;

import java.util.Optional;

@Service
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    private static final String NULL_AUTHENTICATION_MESSAGE = "Authentication is null";

    private static final String EMPTY_PRINCIPAL_MESSAGE = "Principal name is empty";

    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;

    public SpringSecurityAuditorAware(DelegatingSecurityContextHolder delegatingSecurityContextHolder) {
        this.delegatingSecurityContextHolder = delegatingSecurityContextHolder;
    }

    @Override
    public Optional<String> getCurrentAuditor() {

        var authentication = delegatingSecurityContextHolder.getAuthentication();
        Assert.notNull(authentication, NULL_AUTHENTICATION_MESSAGE);

        var principalName = authentication.getName();
        Assert.hasText(principalName, EMPTY_PRINCIPAL_MESSAGE);

        return Optional.of(principalName);
    }

}
