package uk.co.serin.thule.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

public class FeignJwtClientConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public BearerAuthFeignRequestInterceptor bearerAuthFeignRequestInterceptor(
            DelegatingSecurityContextHolder delegatingSecurityContextHolder, OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {

        return new BearerAuthFeignRequestInterceptor(delegatingSecurityContextHolder, oAuth2AuthorizedClientManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                       OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        var tokenResponseClient = new DefaultClientCredentialsTokenResponseClient();
        var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials(
                clientCredentials -> clientCredentials.accessTokenResponseClient(tokenResponseClient)).build();

        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
