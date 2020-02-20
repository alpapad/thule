package uk.co.serin.thule.spring.boot.starter.autoconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.serin.thule.utils.trace.LogExceptionInterceptor;
import uk.co.serin.thule.utils.trace.TracePublicMethodsInterceptor;

@Configuration
public class UtilsAutoConfiguration {
    @Bean
    public LogExceptionInterceptor logExceptionInterceptor() {
        return new LogExceptionInterceptor();
    }

    @Bean
    public TracePublicMethodsInterceptor tracePublicMethodsInterceptor() {
        return new TracePublicMethodsInterceptor();
    }
}
