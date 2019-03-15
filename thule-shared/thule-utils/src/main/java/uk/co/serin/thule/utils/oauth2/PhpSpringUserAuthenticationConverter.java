package uk.co.serin.thule.utils.oauth2;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Collections;
import java.util.Map;

public class PhpSpringUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

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
        if (map.containsKey(SpringJwtAccessTokenConverter.JAVA_USERNAME)) {
            var authentication = super.extractAuthentication(map);

            if (map.containsKey(SpringJwtAccessTokenConverter.JAVA_USERID)) {
                var userId = map.get(SpringJwtAccessTokenConverter.JAVA_USERID).toString();
                var userAuthenticationDetails = new UserAuthenticationDetails(Long.parseLong(userId));
                ((UsernamePasswordAuthenticationToken) authentication).setDetails(userAuthenticationDetails);
            }

            return authentication;
        } else {
            //Extracts data map, assigns the principal and creates a new map in a Spring valid context.
            var data = createDataStructure(map);
            var principal = data.containsKey(SpringJwtAccessTokenConverter.PHP_USERNAME) ? data.get(SpringJwtAccessTokenConverter.PHP_USERNAME).toString() :
                    "unavailable";
            Map<String, Object> mapWithUserName = Collections.singletonMap(SpringJwtAccessTokenConverter.JAVA_USERNAME, principal);

            //Creates authentication object
            var authentication = (UsernamePasswordAuthenticationToken) super.extractAuthentication(mapWithUserName);

            if (!data.containsKey(SpringJwtAccessTokenConverter.PHP_USERID)) {
                throw new UserIdNotFoundException("token does not contain userId");
            }

            var userAuthenticationDetails = new UserAuthenticationDetails(Long.parseLong(data.get(SpringJwtAccessTokenConverter.PHP_USERID).toString()));
            authentication.setDetails(userAuthenticationDetails);
            return authentication;
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> createDataStructure(Map<String, ?> map) {
        return (Map) map.get(SpringJwtAccessTokenConverter.PHP_DATA);
    }
}
