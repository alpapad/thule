package uk.co.serin.thule.gateway;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.NoArgsConstructor;

@Configuration
@EnableDiscoveryClient
@NoArgsConstructor
public class ApplicationConfigurer {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                        .matchers(EndpointRequest.toAnyEndpoint()).permitAll() // allow actuator endpoints, even if not authenticated
                        .pathMatchers("/thule-*-service/**").permitAll() // allow micro-service urls to be unauthenticated (authentication is done in the down-stream micro-service)

        ).anonymous(); // everything else must be forbidden

        return http.build();
    }
}