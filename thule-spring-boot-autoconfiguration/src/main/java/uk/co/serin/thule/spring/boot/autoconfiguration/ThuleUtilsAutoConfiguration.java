package uk.co.serin.thule.spring.boot.autoconfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import uk.co.serin.thule.core.CoreConfiguration;

@Configuration
@Import(CoreConfiguration.class)
public class ThuleUtilsAutoConfiguration {
}
