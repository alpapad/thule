package uk.co.serin.thule.security.oauth2.feign;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class OAuth2FeignRequestInterceptor implements RequestInterceptor {
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = delegatingSecurityContextHolder.getAuthentication();

        if (authentication != null && authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
            template.header(HttpHeaders.AUTHORIZATION, String.format("%s %s", BEARER_TOKEN_TYPE, details.getTokenValue()));
        }
    }
}