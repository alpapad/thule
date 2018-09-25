package com.gohenry.spring.boot.starter.oauth2jpa.autoconfiguration;

import com.gohenry.oauth2jpa.SpringSecurityAuditorAware;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ComponentScan(basePackageClasses = SpringSecurityAuditorAware.class)
@ConditionalOnProperty(name = "gohenry.shared.oauth2.resource-server-enabled", matchIfMissing = true)
@ConditionalOnClass(JpaRepository.class)
@Configuration
@EnableJpaAuditing
public class OAuth2JpaIntegrationAutoConfiguration {
}
