package uk.co.serin.thule.oauth2;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.Map;

public class SpringJwtAccessTokenConverter extends DefaultAccessTokenConverter {

    public static final String JAVA_USERID = "user_id";
    public static final String JAVA_USERNAME = UserAuthenticationConverter.USERNAME;
    public static final String PHP_DATA = "data";
    public static final String PHP_USERID = "userId";
    public static final String PHP_USERNAME = "userName";

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, ?> defaultMap = super.convertAccessToken(token, authentication);
        if (authentication.getDetails() instanceof UserAuthenticationDetails) {
            UserAuthenticationDetails userAuthenticationDetails = (UserAuthenticationDetails) authentication.getDetails();
            ((Map<String, Object>) defaultMap).put(JAVA_USERID, String.valueOf(userAuthenticationDetails.getUserId()));
        }
        return defaultMap;
    }
}
