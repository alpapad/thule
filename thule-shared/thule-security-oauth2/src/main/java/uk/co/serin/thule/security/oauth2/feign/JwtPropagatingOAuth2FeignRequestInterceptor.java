package uk.co.serin.thule.security.oauth2.feign;

import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;

public class JwtPropagatingOAuth2FeignRequestInterceptor extends OAuth2FeignRequestInterceptor {
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;

    public JwtPropagatingOAuth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext, OAuth2ProtectedResourceDetails resource,
                                                       DelegatingSecurityContextHolder delegatingSecurityContextHolder) {
        super(oAuth2ClientContext, resource);
        this.delegatingSecurityContextHolder = delegatingSecurityContextHolder;
    }

    @Override
    protected String extract(String tokenType) {
        var authentication = delegatingSecurityContextHolder.getAuthentication();

        if (authentication != null && authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            var oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
            return String.format("%s %s", tokenType, oAuth2AuthenticationDetails.getTokenValue());
        } else {
            return super.extract(tokenType);
        }
    }
}