package uk.co.serin.thule.repository.mongodb.support;

import com.gohenry.utils.utils.RandomUtils;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RandomUsernameAuditorAware implements AuditorAware<String> {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(RandomUtils.generateUniqueRandomString());
    }
}