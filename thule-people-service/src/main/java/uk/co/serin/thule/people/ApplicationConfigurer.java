package uk.co.serin.thule.people;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import uk.co.serin.thule.people.repository.support.ThuleJpaRepository;
import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;
import uk.co.serin.thule.security.oauth2.feign.JwtPropagatingOAuth2FeignRequestInterceptor;

import lombok.NoArgsConstructor;

@Configuration
@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
@EnableJpaRepositories(repositoryBaseClass = ThuleJpaRepository.class)
@NoArgsConstructor
public class ApplicationConfigurer {
    static {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public JwtPropagatingOAuth2FeignRequestInterceptor jwtPropagatingOAuth2FeignRequestInterceptor(
            DelegatingSecurityContextHolder delegatingSecurityContextHolder, OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails) {
        return new JwtPropagatingOAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), oAuth2ProtectedResourceDetails,
                delegatingSecurityContextHolder);
    }
}
