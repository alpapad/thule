package uk.co.serin.thule.shared.oauth2;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import uk.co.serin.thule.oauth2.UserAuthenticationDetails;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringPhpUserAuthenticationConverterTest {

    private static final String USERNAME = "userName";
    private static final String USER_ID = "user_id";

    @Test
    public void when_given_authentication_object_then_convert_to_map() {
        //Given
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(USERNAME, "password");
        UserAuthenticationDetails userAuthenticationDetails = new UserAuthenticationDetails(999);
        token.setDetails(userAuthenticationDetails);
        SpringPhpUserAuthenticationConverter springPhpUserAuthenticationConverter = new SpringPhpUserAuthenticationConverter();

        //When
        Map map = springPhpUserAuthenticationConverter.convertUserAuthentication(token);

        //Then
        Map data = (Map) map.get("data");
        assertThat(data.get(USERNAME)).isEqualTo(USERNAME);
        assertThat(data.get(USER_ID)).isEqualTo(999L);
    }
}
