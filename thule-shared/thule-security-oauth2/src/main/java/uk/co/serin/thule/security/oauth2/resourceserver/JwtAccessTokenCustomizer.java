package uk.co.serin.thule.security.oauth2.resourceserver;

import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import uk.co.serin.thule.security.oauth2.context.UserAuthenticationDetails;

import java.util.HashMap;
import java.util.Map;

public class JwtAccessTokenCustomizer extends DefaultAccessTokenConverter implements JwtAccessTokenConverterConfigurer {
    public static final String JAVA_USER_NAME = UserAuthenticationConverter.USERNAME;
    public static final String JAVA_USER_ID = "user_id";
    public static final String PHP_DATA_STRUCTURE = "data";
    public static final String PHP_USER_NAME = "userName";
    public static final String PHP_USER_ID = "userId";

    @Override
    public void configure(JwtAccessTokenConverter converter) {
        converter.setAccessTokenConverter(this);
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> jwtAsMap) {
        if (jwtAsMap.containsKey(PHP_DATA_STRUCTURE)) {
            var javaJwt = convertPhpJwtToJavaJwt(jwtAsMap);
            return convertJavaJwtToOauth2Authentication(javaJwt);
        } else {
            return convertJavaJwtToOauth2Authentication(jwtAsMap);
        }
    }

    private Map<String, ?> convertPhpJwtToJavaJwt(Map<String, ?> jwtAsMap) {
        var phpJwtDataStructure = retrievePhpJwtDataDataStructure(jwtAsMap);
        var convertedJwt = new HashMap<String, Object>(jwtAsMap);

        if (phpJwtDataStructure.containsKey(PHP_USER_NAME)) {
            var userName = phpJwtDataStructure.get(PHP_USER_NAME);
            convertedJwt.putIfAbsent(JAVA_USER_NAME, userName);
        }

        if (phpJwtDataStructure.containsKey(PHP_USER_ID)) {
            var userId = phpJwtDataStructure.get(PHP_USER_ID);
            convertedJwt.putIfAbsent(JAVA_USER_ID, userId);
        }

        convertedJwt.remove(PHP_DATA_STRUCTURE);
        return convertedJwt;
    }

    private OAuth2Authentication convertJavaJwtToOauth2Authentication(Map<String, ?> jwtAsMap) {
        var oAuth2Authentication = super.extractAuthentication(jwtAsMap);

        if (!oAuth2Authentication.isClientOnly() && jwtAsMap.containsKey(JAVA_USER_ID)) {
            var userId = jwtAsMap.get(JAVA_USER_ID).toString();

            return createOauth2AuthenticationWithUserId(oAuth2Authentication, userId);
        } else {
            return oAuth2Authentication;
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> retrievePhpJwtDataDataStructure(Map<String, ?> map) {
        return (Map<String, ?>) map.get(PHP_DATA_STRUCTURE);
    }

    private OAuth2Authentication createOauth2AuthenticationWithUserId(OAuth2Authentication oAuth2Authentication, String userId) {
        // Insert the user id into a UserAuthenticationDetails
        var userAuthenticationDetails = UserAuthenticationDetails.builder().userId(Long.parseLong(userId)).build();

        // Recreate the user Authentication with the UserAuthenticationDetails (i.e. user id)
        var userAuthenticationWithoutUserId = oAuth2Authentication.getUserAuthentication();
        var userAuthenticationWithUserId = new UsernamePasswordAuthenticationToken(userAuthenticationWithoutUserId.getName(),
                userAuthenticationWithoutUserId.getCredentials(), userAuthenticationWithoutUserId.getAuthorities());
        userAuthenticationWithUserId.setDetails(userAuthenticationDetails);

        // Return the new OAuth2Authentication containing the UserAuthenticationDetails (i.e. user id)
        return new OAuth2Authentication(oAuth2Authentication.getOAuth2Request(), userAuthenticationWithUserId);
    }
}