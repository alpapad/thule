package uk.co.serin.thule.oauth2;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.serin.thule.oauth2.SpringJwtAccessTokenConverter.PHP_USERID;

public class SpringJwtAccessTokenConverterTest {

    private SpringJwtAccessTokenConverter sut = new SpringJwtAccessTokenConverter();

    @Test
    public void given_non_user_authentication_details_object_contained_in_details_then_map_returned_excludes_user_id() {
        //Given
        OAuth2Request oAuth2Request = new OAuth2Request(null, String.valueOf(1234567890), null,
                true, null, null, null, null, null);
        UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken("", "", Collections.EMPTY_LIST);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, userAuthentication);

        // Create OAuth2AccessToken
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(new InMemoryTokenStore());
        OAuth2AccessToken oAuth2AccessToken = defaultTokenServices.createAccessToken(oAuth2Authentication);

        //When
        Map<String, ?> actual = sut.convertAccessToken(oAuth2AccessToken, oAuth2Authentication);

        //Then
        assertThat(actual).isNotNull();
        assertThat(actual).isNotEmpty();
        assertThat(actual).doesNotContainKey(PHP_USERID);
    }
}
