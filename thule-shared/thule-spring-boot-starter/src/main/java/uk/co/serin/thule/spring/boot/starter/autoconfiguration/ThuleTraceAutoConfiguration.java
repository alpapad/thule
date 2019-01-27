package uk.co.serin.thule.spring.boot.starter.autoconfiguration;

import uk.co.serin.thule.utils.service.trace.TraceConfigurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TraceConfigurer.class)
public class ThuleTraceAutoConfiguration {
}
