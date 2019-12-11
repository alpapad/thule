package uk.co.serin.thule.security.oauth2.resourceserver;

import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import uk.co.serin.thule.security.oauth2.context.UserIdEnhancedUserAuthenticationConverter;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtAccessTokenCustomizer extends DefaultAccessTokenConverter implements JwtAccessTokenConverterConfigurer {
    private UserIdEnhancedUserAuthenticationConverter userIdEnhancedUserAuthenticationConverter;

    @Override
    public void configure(JwtAccessTokenConverter converter) {
        converter.setAccessTokenConverter(this);
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> jwtClaims) {
        var oAuth2Authentication = super.extractAuthentication(jwtClaims);
        var userAuthentication = userIdEnhancedUserAuthenticationConverter.extractAuthentication(jwtClaims);

        return new OAuth2Authentication(oAuth2Authentication.getOAuth2Request(), userAuthentication);
    }
}