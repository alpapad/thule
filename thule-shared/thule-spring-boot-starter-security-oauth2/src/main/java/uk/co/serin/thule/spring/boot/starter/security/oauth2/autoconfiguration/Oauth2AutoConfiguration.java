package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import uk.co.serin.thule.security.oauth2.resourceserver.JwtAccessTokenCustomizer;
import uk.co.serin.thule.security.oauth2.resourceserver.JwtResourceServerConfigurerAdapter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = "thule.shared.oauth2.resource.enabled", matchIfMissing = true)
@Configuration
@Import(JwtResourceServerConfigurerAdapter.class)
public class Oauth2AutoConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(), HttpMethod.POST.name());
            }
        };
    }

    @Bean
    public JwtAccessTokenCustomizer jwtAccessTokenCustomizer() {
        return new JwtAccessTokenCustomizer();
    }
}
