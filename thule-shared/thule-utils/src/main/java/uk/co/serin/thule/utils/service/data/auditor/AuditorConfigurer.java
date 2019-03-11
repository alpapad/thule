package uk.co.serin.thule.utils.service.data.auditor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.NoArgsConstructor;

@Configuration
@ComponentScan
@EnableJpaAuditing
@NoArgsConstructor
public class AuditorConfigurer {
}

