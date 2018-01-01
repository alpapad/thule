package uk.co.serin.thule.spring.boot.starter.utils.autoconfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import uk.co.serin.thule.utils.UtilsConfigurer;

@Configuration
@Import(UtilsConfigurer.class)
public class ThuleUtilsAutoConfiguration {
}
