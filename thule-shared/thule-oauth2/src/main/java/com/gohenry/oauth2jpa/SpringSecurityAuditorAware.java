package com.gohenry.oauth2jpa;

import com.gohenry.oauth2.DelegatingSecurityContextHolder;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    private static final String NULL_AUTHENTICATION_MESSAGE = "Authentication is null";

    private static final String EMPTY_PRINCIPAL_MESSAGE = "Principal name is empty";

    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;

    public SpringSecurityAuditorAware(DelegatingSecurityContextHolder delegatingSecurityContextHolder) {
        this.delegatingSecurityContextHolder = delegatingSecurityContextHolder;
    }

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = delegatingSecurityContextHolder.getAuthentication();
        Assert.notNull(authentication, NULL_AUTHENTICATION_MESSAGE);

        String principalName = authentication.getName();
        Assert.hasText(principalName, EMPTY_PRINCIPAL_MESSAGE);

        return Optional.of(principalName);
    }

}
