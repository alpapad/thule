package uk.co.serin.thule.spring.boot.starter.security.oauth2.testservice;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;
import uk.co.serin.thule.security.oauth2.context.Oauth2DelegatingSecurityContextHolder;
import uk.co.serin.thule.security.oauth2.feign.JwtPropagatingOAuth2FeignRequestInterceptor;

@Configuration
@EnableFeignClients
@EnableResourceServer
public class ApplicationConfigurer {
    @Bean
    public Oauth2DelegatingSecurityContextHolder delegatingSecurityContextHolder() {
        return new Oauth2DelegatingSecurityContextHolder();
    }

    @Bean
    public JwtPropagatingOAuth2FeignRequestInterceptor jwtPropagatingOAuth2FeignRequestInterceptor(
            DelegatingSecurityContextHolder delegatingSecurityContextHolder, OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails) {
        return new JwtPropagatingOAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), oAuth2ProtectedResourceDetails,
                delegatingSecurityContextHolder);
    }
}
