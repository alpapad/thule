package uk.co.serin.thule.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

import lombok.NoArgsConstructor;

@Configuration
@EnableAdminServer
@EnableDiscoveryClient
@EnableScheduling
@NoArgsConstructor
public class ApplicationConfigurer {
    @Bean
    public ForwardedHeaderTransformer forwardedHeaderTransformer() {
        return new ForwardedHeaderTransformer();
    }
}