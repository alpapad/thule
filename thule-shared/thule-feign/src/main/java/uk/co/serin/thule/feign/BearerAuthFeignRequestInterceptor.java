package uk.co.serin.thule.feign;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.Assert;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BearerAuthFeignRequestInterceptor implements RequestInterceptor {
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + extractJwtValue());
    }

    private String extractJwtValue() {
        var authentication = delegatingSecurityContextHolder.getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            return extractJwtValueFromJwtAuthenticationToken((JwtAuthenticationToken) authentication);
        } else {
            return retrieveJwtValueFromOauth2Client();
        }
    }

    private String extractJwtValueFromJwtAuthenticationToken(JwtAuthenticationToken authentication) {
        return authentication.getToken().getTokenValue();
    }

    private String retrieveJwtValueFromOauth2Client() {
        Authentication principal = new AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        var authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak").principal(principal).build();
        var authorizedClient = oAuth2AuthorizedClientManager.authorize(authorizeRequest);

        Assert.notNull(authorizedClient, "authorizedClient is null");
        Assert.notNull(authorizedClient.getAccessToken(), "accessToken is null");

        return authorizedClient.getAccessToken().getTokenValue();
    }
}