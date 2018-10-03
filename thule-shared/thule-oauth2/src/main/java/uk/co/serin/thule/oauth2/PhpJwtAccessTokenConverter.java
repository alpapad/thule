package uk.co.serin.thule.oauth2;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.Map;

import static uk.co.serin.thule.oauth2.SpringJwtAccessTokenConverter.PHP_USERID;

public class PhpJwtAccessTokenConverter extends DefaultAccessTokenConverter {

    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, ?> defaultMap = super.convertAccessToken(token, authentication);
        if (authentication.getUserAuthentication().getDetails() instanceof UserAuthenticationDetails) {
            UserAuthenticationDetails userAuthenticationDetails = (UserAuthenticationDetails) authentication.getUserAuthentication().getDetails();
            ((Map<String, Object>) defaultMap).put(PHP_USERID, String.valueOf(userAuthenticationDetails.getUserId()));
        }
        return defaultMap;
    }
}
