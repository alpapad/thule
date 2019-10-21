package uk.co.serin.thule.gateway;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.NoArgsConstructor;

@Configuration
@EnableAsync
@EnableZuulProxy
@NoArgsConstructor
public class ApplicationConfigurer {
}