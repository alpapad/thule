package uk.co.serin.thule.gateway;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;

import lombok.Generated;
import lombok.NoArgsConstructor;

@Configuration
@EnableDiscoveryClient
@NoArgsConstructor
@Generated
public class ApplicationConfigurer {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository clientRegistrationRepository) {
        // Authenticate through configured OpenID Provider
        http.oauth2Login();

        // Also logout at the OpenID Connect provider
        http.logout(logout -> logout.logoutSuccessHandler(new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository)));

        // Require authentication for all requests
        http.authorizeExchange().anyExchange().authenticated();

        // Allow showing /home within a frame
        http.headers().frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN);

        // Disable CSRF in the gateway to prevent conflicts with proxied service CSRF
        http.csrf().disable();

        return http.build();
    }
}