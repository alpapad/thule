package uk.co.serin.thule.spring.boot.starter.security.oauth2.autoconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import uk.co.serin.thule.security.oauth2.PhpSpringUserAuthenticationConverter;
import uk.co.serin.thule.security.oauth2.SpringJwtAccessTokenConverter;

@ConditionalOnProperty(name = "thule.shared.oauth2.resource-server-enabled", matchIfMissing = true)
@Configuration
@EnableConfigurationProperties(Oauth2Properties.class)
@Import(JwtResourceServerConfigurerAdapter.class)
public class Oauth2AutoConfiguration {
    private Oauth2Properties oauthProperties;

    public Oauth2AutoConfiguration(Oauth2Properties oauthProperties) {
        this.oauthProperties = oauthProperties;
    }

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
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        SpringJwtAccessTokenConverter springJwtAccessTokenConverter = new SpringJwtAccessTokenConverter();
        PhpSpringUserAuthenticationConverter javaPhpAuthenticationConverter = new PhpSpringUserAuthenticationConverter();

        springJwtAccessTokenConverter.setUserTokenConverter(javaPhpAuthenticationConverter);

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(oauthProperties.getSigningKey());
        converter.setAccessTokenConverter(springJwtAccessTokenConverter);
        return converter;
    }
}
