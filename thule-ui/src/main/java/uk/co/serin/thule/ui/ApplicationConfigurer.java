package uk.co.serin.thule.ui;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

import lombok.NoArgsConstructor;

@Configuration
@EnableDiscoveryClient
@NoArgsConstructor
@SuppressWarnings("squid:S1118") // Suppress Utility classes should not have public constructors
public class ApplicationConfigurer {
}
