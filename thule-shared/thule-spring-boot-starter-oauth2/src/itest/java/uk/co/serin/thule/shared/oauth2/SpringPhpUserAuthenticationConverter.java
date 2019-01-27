package uk.co.serin.thule.shared.oauth2;

import org.springframework.security.core.Authentication;

import uk.co.serin.thule.utils.oauth2.PhpSpringUserAuthenticationConverter;
import uk.co.serin.thule.utils.oauth2.SpringJwtAccessTokenConverter;
import uk.co.serin.thule.utils.oauth2.UserAuthenticationDetails;

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
        data.put(SpringJwtAccessTokenConverter.PHP_USERNAME, authentication.getName());
        UserAuthenticationDetails userAuthenticationDetails = (UserAuthenticationDetails) authentication.getDetails();
        data.put(SpringJwtAccessTokenConverter.PHP_USERID, userAuthenticationDetails.getUserId());
        response.put(SpringJwtAccessTokenConverter.PHP_DATA, data);

        return response;
    }
}
