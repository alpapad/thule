package uk.co.serin.thule.spring.boot.starter.autoconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.utils.trace.TracePublicMethodsInterceptor;

import lombok.NoArgsConstructor;

@Configuration
@NoArgsConstructor
public class UtilsAutoConfiguration {
    @Bean
    public TracePublicMethodsInterceptor tracePublicMethodsInterceptor() {
        return new TracePublicMethodsInterceptor();
    }
}
