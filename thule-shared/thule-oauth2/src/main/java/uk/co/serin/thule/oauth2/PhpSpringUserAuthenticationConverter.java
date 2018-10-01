package uk.co.serin.thule.oauth2;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Collections;
import java.util.Map;

public class PhpSpringUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    protected static final String PHP_USER_NAME = "userName";
    protected static final String USER_ID = "user_id";

    /**
     * Creates an Authentication object depending on if the Map argument is Java or PHP based.
     *
     * If Map is Java based:
     * Use the super method
     * If Map is PHP based:
     * extract the data HashMap from the argument
     * ascertain the user_name field as defined or unavailable
     * create a new map containing the user_name field & then use the super method
     * Create a UserAuthenticationDetails object using the userId field
     * set the UserAuthenticationDetails object in the Authentication.setDetails()
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Authentication authentication = super.extractAuthentication(map);
            String userId = map.get(USER_ID).toString();
            UserAuthenticationDetails userAuthenticationDetails = new UserAuthenticationDetails(Long.parseLong(userId));
            ((UsernamePasswordAuthenticationToken) authentication).setDetails(userAuthenticationDetails);
            return authentication;
        } else {
            //Extracts data map, assigns the principal and creates a new map in a Spring valid context.
            Map<String, Object> data = createDataStructure(map);
            String principal = data.containsKey(PHP_USER_NAME) ? data.get(PHP_USER_NAME).toString() : "unavailable";
            Map<String, Object> mapWithUserName = Collections.singletonMap(USERNAME, principal);

            //Creates authentication object
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) super.extractAuthentication(mapWithUserName);

            if (!data.containsKey(USER_ID)) {
                throw new UserIdNotFoundException("token does not contain userId");
            }

            UserAuthenticationDetails userAuthenticationDetails = new UserAuthenticationDetails(Long.parseLong(data.get(USER_ID).toString()));
            authentication.setDetails(userAuthenticationDetails);
            return authentication;
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> createDataStructure(Map<String, ?> map) {
        return (Map) map.get("data");
    }
}
