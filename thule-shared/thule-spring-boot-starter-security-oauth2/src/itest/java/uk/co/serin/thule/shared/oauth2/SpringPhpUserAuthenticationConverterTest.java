package uk.co.serin.thule.shared.oauth2;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import uk.co.serin.thule.security.UserAuthenticationDetails;
import uk.co.serin.thule.security.oauth2.SpringJwtAccessTokenConverter;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringPhpUserAuthenticationConverterTest {
    @Test
    public void when_given_authentication_object_then_convert_to_map() {
        // Given
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(SpringJwtAccessTokenConverter.PHP_USERNAME, "password");
        UserAuthenticationDetails userAuthenticationDetails = UserAuthenticationDetails.builder().userId(999).build();
        token.setDetails(userAuthenticationDetails);
        SpringPhpUserAuthenticationConverter springPhpUserAuthenticationConverter = new SpringPhpUserAuthenticationConverter();

        // When
        Map map = springPhpUserAuthenticationConverter.convertUserAuthentication(token);

        // Then
        Map data = (Map) map.get("data");
        assertThat(data.get(SpringJwtAccessTokenConverter.PHP_USERNAME)).isEqualTo(SpringJwtAccessTokenConverter.PHP_USERNAME);
        assertThat(data.get(SpringJwtAccessTokenConverter.PHP_USERID)).isEqualTo(999L);
    }
}
