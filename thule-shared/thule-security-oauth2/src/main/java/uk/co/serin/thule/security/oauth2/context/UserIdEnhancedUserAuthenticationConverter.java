package uk.co.serin.thule.security.oauth2.context;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import uk.co.serin.thule.security.context.UserAuthenticationDetails;

import java.util.HashMap;
import java.util.Map;

public class UserIdEnhancedUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
    public static final String JAVA_USER_ID = "user_id";
    public static final String JAVA_USER_NAME = USERNAME;
    public static final String PHP_DATA_STRUCTURE = "data";
    public static final String PHP_USER_ID = "userId";
    public static final String PHP_USER_NAME = "userName";

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        var convertedUserAuthentication = new HashMap<String, Object>(super.convertUserAuthentication(authentication));
        var details = authentication.getDetails();
        if (details instanceof UserAuthenticationDetails) {
            var userAuthenticationDetails = (UserAuthenticationDetails) details;
            convertedUserAuthentication.put(JAVA_USER_ID, userAuthenticationDetails.getUserId());
        }

        return convertedUserAuthentication;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> authenticationSource) {
        if (authenticationSource.containsKey(PHP_DATA_STRUCTURE)) {
            var javaFormatAuthenticationSource = convertPhpFormatToJavaFormat(authenticationSource);
            return convertJavaFormatToAuthentication(javaFormatAuthenticationSource);
        } else {
            return convertJavaFormatToAuthentication(authenticationSource);
        }
    }

    private Map<String, ?> convertPhpFormatToJavaFormat(Map<String, ?> phpFormatAuthenticationSource) {
        var phpDataStructure = retrievePhpDataStructure(phpFormatAuthenticationSource);
        var javaFormatAuthenticationSource = new HashMap<String, Object>(phpFormatAuthenticationSource);

        if (phpDataStructure.containsKey(PHP_USER_NAME)) {
            var userName = phpDataStructure.get(PHP_USER_NAME);
            javaFormatAuthenticationSource.putIfAbsent(JAVA_USER_NAME, userName);
        }

        if (phpDataStructure.containsKey(PHP_USER_ID)) {
            var userId = phpDataStructure.get(PHP_USER_ID);
            javaFormatAuthenticationSource.putIfAbsent(JAVA_USER_ID, userId);
        }

        javaFormatAuthenticationSource.remove(PHP_DATA_STRUCTURE);
        return javaFormatAuthenticationSource;
    }

    private Authentication convertJavaFormatToAuthentication(Map<String, ?> javaFormatAuthenticationSource) {
        var oAuth2Authentication = super.extractAuthentication(javaFormatAuthenticationSource);

        if (javaFormatAuthenticationSource.containsKey(JAVA_USER_ID)) {
            var userId = javaFormatAuthenticationSource.get(JAVA_USER_ID).toString();

            return createAuthenticationWithUserId(oAuth2Authentication, userId);
        } else {
            return oAuth2Authentication;
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> retrievePhpDataStructure(Map<String, ?> phpFormatAuthenticationSource) {
        return (Map<String, ?>) phpFormatAuthenticationSource.get(PHP_DATA_STRUCTURE);
    }

    private Authentication createAuthenticationWithUserId(Authentication authentication, String userId) {
        // Insert the user id into a UserAuthenticationDetails
        var userAuthenticationDetails = UserAuthenticationDetails.builder().userId(Long.parseLong(userId)).build();

        // Recreate the user Authentication with the UserAuthenticationDetails (i.e. user id)
        var userAuthenticationWithUserId = new UsernamePasswordAuthenticationToken(authentication.getName(),
                authentication.getCredentials(), authentication.getAuthorities());
        userAuthenticationWithUserId.setDetails(userAuthenticationDetails);

        return userAuthenticationWithUserId;
    }
}