package uk.co.serin.thule.utils.service.data.auditor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@ComponentScan
@EnableJpaAuditing
public class AuditorConfigurer {
}

