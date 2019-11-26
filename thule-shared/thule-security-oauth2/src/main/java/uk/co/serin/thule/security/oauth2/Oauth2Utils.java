package uk.co.serin.thule.security.oauth2;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import uk.co.serin.thule.security.UserAuthenticationDetails;

import java.util.Collection;

public interface Oauth2Utils {
    static OAuth2AccessToken createJwtOauth2AccessToken(String principal, String credentials, long userId,
                                                        Collection<? extends GrantedAuthority> grantedAuthorities,
                                                        String clientId, String signingKey) {
        // Create OAuth2Authentication
        var oAuth2Request = new OAuth2Request(null, clientId, null,
                true, null, null, null, null, null);
        var userAuthentication = new UsernamePasswordAuthenticationToken(principal, credentials, grantedAuthorities);

        //Add UserAuthenticationDetails
        var userAuthenticationDetails = UserAuthenticationDetails.builder().userId(userId).build();
        userAuthentication.setDetails(userAuthenticationDetails);

        var oAuth2Authentication = new OAuth2Authentication(oAuth2Request, userAuthentication);
        oAuth2Authentication.setDetails(userAuthenticationDetails);

        // Create OAuth2AccessToken
        var defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(new InMemoryTokenStore());
        var oAuth2AccessToken = defaultTokenServices.createAccessToken(oAuth2Authentication);

        // Create JwtAccessTokenConverter
        var jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(signingKey);
        jwtAccessTokenConverter.setAccessTokenConverter(new SpringJwtAccessTokenConverter());

        // Convert the oAuth2AccessToken to JWT format and return it
        return jwtAccessTokenConverter.enhance(oAuth2AccessToken, oAuth2Authentication);
    }
}
