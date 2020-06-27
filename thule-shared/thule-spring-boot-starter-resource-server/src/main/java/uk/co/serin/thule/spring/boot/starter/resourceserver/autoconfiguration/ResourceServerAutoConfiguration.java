package uk.co.serin.thule.spring.boot.starter.resourceserver.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import uk.co.serin.thule.resourceserver.converter.KeycloakResourceRoleConverter;
import uk.co.serin.thule.resourceserver.converter.KeycloakSubjectClaimConverter;
import uk.co.serin.thule.resourceserver.decoder.KeycloakUnsignedJwtDecoder;

import lombok.Generated;

@AutoConfigureBefore(OAuth2ResourceServerAutoConfiguration.class)
@ConditionalOnProperty(name = "thule.shared.oauth2.resourceserver.enabled", matchIfMissing = true)
@Configuration
@EnableWebSecurity
@Order(99)
public class ResourceServerAutoConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    @ConditionalOnProperty(value = "thule.shared.oauth2.resourceserver.jws.enabled", havingValue = "false")
    public JwtDecoder jwtDecoder() {
        return new KeycloakUnsignedJwtDecoder();
    }

    @Bean
    @ConditionalOnProperty({"spring.security.oauth2.resourceserver.jwt.issuer-uri", "thule.shared.oauth2.resourceserver.jws.enabled"})
    public JwtDecoder jwtDecoderFromIssuerLocation(OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
        var issuerUri = oAuth2ResourceServerProperties.getJwt().getIssuerUri();
        var jwtDecoder = getJwtDecoderfromIssuerLocation(issuerUri);
        jwtDecoder.setClaimSetConverter(new KeycloakSubjectClaimConverter());
        return jwtDecoder;
    }

    /**
     * Marked as generated because cannot mock a static method (without going through immense pain)
     */
    @Generated
    NimbusJwtDecoder getJwtDecoderfromIssuerLocation(String issuerUri) {
        return (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and() // allow pre-flight CORS requests
                    .authorizeRequests() // specify requests that are authorised
                    .antMatchers("/actuator/**").permitAll() // allow actuator endpoints, even if not authenticated
                    .antMatchers("/v2/api-docs").permitAll() // allow swagger docs, even if not authenticated
                    .antMatchers("/**").authenticated().and() // everything else must be authenticated
                    .oauth2ResourceServer().jwt() // configure oauth2 resource server using JWTs
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()); // map keycloak roles to Spring Security granted authorities
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(keycloakResourceRoleConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    public KeycloakResourceRoleConverter keycloakResourceRoleConverter() {
        return new KeycloakResourceRoleConverter(getApplicationContext().getEnvironment());
    }
}
