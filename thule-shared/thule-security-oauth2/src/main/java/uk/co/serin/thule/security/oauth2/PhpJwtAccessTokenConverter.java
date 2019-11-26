package uk.co.serin.thule.security.oauth2;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import uk.co.serin.thule.security.UserAuthenticationDetails;

import java.util.Map;

import static uk.co.serin.thule.security.oauth2.SpringJwtAccessTokenConverter.PHP_USERID;

public class PhpJwtAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        var defaultMap = (Map<String, Object>) super.convertAccessToken(token, authentication);
        if (authentication.getUserAuthentication().getDetails() instanceof UserAuthenticationDetails) {
            var userAuthenticationDetails = (UserAuthenticationDetails) authentication.getUserAuthentication().getDetails();
            defaultMap.put(PHP_USERID, String.valueOf(userAuthenticationDetails.getUserId()));
        }
        return defaultMap;
    }
}
