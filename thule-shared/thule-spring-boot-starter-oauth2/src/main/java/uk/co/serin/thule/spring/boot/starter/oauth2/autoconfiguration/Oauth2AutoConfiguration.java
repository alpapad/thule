package uk.co.serin.thule.spring.boot.starter.oauth2.autoconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import uk.co.serin.thule.oauth2.Oauth2Configurer;
import uk.co.serin.thule.oauth2.PhpSpringUserAuthenticationConverter;
import uk.co.serin.thule.oauth2.UserIdJwtAccessTokenConverter;

@ComponentScan
@ConditionalOnProperty(name = "thule.shared.oauth2.resource-server-enabled", matchIfMissing = true)
@Configuration
@EnableConfigurationProperties(uk.co.serin.thule.spring.boot.starter.oauth2.autoconfiguration.Oauth2Properties.class)
@Import(Oauth2Configurer.class)
public class Oauth2AutoConfiguration {
    private uk.co.serin.thule.spring.boot.starter.oauth2.autoconfiguration.Oauth2Properties oauthProperties;

    public Oauth2AutoConfiguration(uk.co.serin.thule.spring.boot.starter.oauth2.autoconfiguration.Oauth2Properties oauthProperties) {
        this.oauthProperties = oauthProperties;
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
        UserIdJwtAccessTokenConverter userIdJwtAccessTokenConverter = new UserIdJwtAccessTokenConverter();
        PhpSpringUserAuthenticationConverter javaPhpAuthenticationConverter = new PhpSpringUserAuthenticationConverter();

        userIdJwtAccessTokenConverter.setUserTokenConverter(javaPhpAuthenticationConverter);

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(oauthProperties.getSigningKey());
        converter.setAccessTokenConverter(userIdJwtAccessTokenConverter);
        return converter;
    }
}
