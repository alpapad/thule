package uk.co.serin.thule.data.audit;

import org.springframework.data.domain.AuditorAware;

import uk.co.serin.thule.security.oauth2.context.DelegatingSecurityContextHolder;

import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;

    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = delegatingSecurityContextHolder.getAuthentication();
        return authentication != null ? Optional.ofNullable(authentication.getName()) : Optional.empty();
    }
}