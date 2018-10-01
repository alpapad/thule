package uk.co.serin.thule.shared.oauth2;

import uk.co.serin.thule.oauth2.PhpSpringUserAuthenticationConverter;
import uk.co.serin.thule.oauth2.UserAuthenticationDetails;

import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is an extension of the PhpSpringUserAuthenticationConverter class located in
 * PhpSpringUserAuthenticationConverter.
 *
 * Provides an additional method of convertUserAuthentication since this method intecepted
 * production environment code where it was only wanted in test environment code.
 */
public class SpringPhpUserAuthenticationConverter extends PhpSpringUserAuthenticationConverter {

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();

        //Create data Map containing userId & refreshAfter
        //Place inside response to replicate as close
        //to PHP based token
        Map<String, Object> data = new HashMap<>();
        data.put(PHP_USER_NAME, authentication.getName());
        UserAuthenticationDetails userAuthenticationDetails = (UserAuthenticationDetails) authentication.getDetails();
        data.put(USER_ID, userAuthenticationDetails.getUserId());
        response.put("data", data);

        return response;
    }
}
