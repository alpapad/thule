package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import uk.co.serin.thule.security.oauth2.resourceserver.JwtAccessTokenCustomizer;
import uk.co.serin.thule.security.oauth2.resourceserver.JwtResourceServerConfigurerAdapter;

import lombok.NoArgsConstructor;

@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = "thule.shared.oauth2.resource.enabled", matchIfMissing = true)
@Configuration
@EnableResourceServer
@NoArgsConstructor
public class ResourceServerAutoConfiguration {
    @Bean
    public JwtAccessTokenCustomizer jwtAccessTokenCustomizer() {
        return new JwtAccessTokenCustomizer();
    }

    @Bean
    public JwtResourceServerConfigurerAdapter jwtResourceServerConfigurerAdapter() {
        return new JwtResourceServerConfigurerAdapter();
    }
}
