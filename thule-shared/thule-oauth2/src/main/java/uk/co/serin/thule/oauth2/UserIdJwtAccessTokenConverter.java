package uk.co.serin.thule.oauth2;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.Map;

public class UserIdJwtAccessTokenConverter extends DefaultAccessTokenConverter {

    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, ?> defaultMap = super.convertAccessToken(token, authentication);
        if (authentication.getDetails() instanceof UserAuthenticationDetails) {
            UserAuthenticationDetails userAuthenticationDetails = (UserAuthenticationDetails) authentication.getDetails();
            ((Map<String, Object>) defaultMap).put("user_id", String.valueOf(userAuthenticationDetails.getUserId()));
        }
        return defaultMap;
    }
}
