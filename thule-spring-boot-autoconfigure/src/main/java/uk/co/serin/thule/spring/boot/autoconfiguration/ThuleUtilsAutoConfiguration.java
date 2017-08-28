package uk.co.serin.thule.spring.boot.autoconfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import uk.co.serin.thule.utils.UtilsConfiguration;

@Configuration
@Import(UtilsConfiguration.class)
public class ThuleUtilsAutoConfiguration {
}
