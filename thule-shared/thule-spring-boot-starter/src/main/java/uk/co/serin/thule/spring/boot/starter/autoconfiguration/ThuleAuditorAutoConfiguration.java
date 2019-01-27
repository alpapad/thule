package uk.co.serin.thule.spring.boot.starter.autoconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;

import uk.co.serin.thule.utils.service.data.auditor.AuditorConfigurer;

@ConditionalOnClass({AuditorAware.class, Authentication.class})
@Configuration
@EnableJpaAuditing
@Import(AuditorConfigurer.class)
public class ThuleAuditorAutoConfiguration {
}
