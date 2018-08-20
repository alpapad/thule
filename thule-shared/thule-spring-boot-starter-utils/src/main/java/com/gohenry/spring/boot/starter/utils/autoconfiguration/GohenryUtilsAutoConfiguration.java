package com.gohenry.spring.boot.starter.utils.autoconfiguration;

import com.gohenry.utils.UtilsConfigurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(UtilsConfigurer.class)
public class GohenryUtilsAutoConfiguration {
}
