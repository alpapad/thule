package uk.co.serin.thule.repository.mongodb.support;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import uk.co.serin.thule.core.utils.RandomGenerators;

@Component
public class RandomUsernameAuditorAware implements AuditorAware<String> {
    @Override
    public String getCurrentAuditor() {
        return RandomGenerators.generateUniqueRandomString();
    }
}