package uk.co.serin.thule.spring.boot.starter.autoconfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import uk.co.serin.thule.utils.service.trace.TraceConfigurer;

import lombok.NoArgsConstructor;

@Configuration
@Import(TraceConfigurer.class)
@NoArgsConstructor
public class ThuleTraceAutoConfiguration {
}
