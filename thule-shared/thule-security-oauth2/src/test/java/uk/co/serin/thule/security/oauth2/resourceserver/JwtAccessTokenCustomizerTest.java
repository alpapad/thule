package uk.co.serin.thule.security.oauth2.resourceserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import uk.co.serin.thule.security.context.UserAuthenticationDetails;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JwtAccessTokenCustomizerTest {
    @Mock
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @InjectMocks
    private JwtAccessTokenCustomizer sut;

    @Test
    public void given_jwt_with_only_client_id_and_no_user_id_when_extracting_authentication_then_an_authentication_without_user_authentication_is_created() {
        // Given
        var jwtAsMap = Map.<String, Object>of();

        // When
        var oAuth2Authentication = sut.extractAuthentication(jwtAsMap);

        // Then
        assertThat(oAuth2Authentication).isNotNull();
        assertThat(oAuth2Authentication.getUserAuthentication()).isNull();
    }

    @Test
    public void given_jwt_with_only_client_id_and_user_id_when_extracting_authentication_then_an_authentication_without_user_authentication_is_created() {
        // Given
        var userId = 123456789;
        var jwtAsMap = Map.<String, Object>of(JwtAccessTokenCustomizer.JAVA_USER_ID, userId);

        // When
        var oAuth2Authentication = sut.extractAuthentication(jwtAsMap);

        // Then
        assertThat(oAuth2Authentication).isNotNull();
        assertThat(oAuth2Authentication.getUserAuthentication()).isNull();
    }

    @Test
    public void given_java_generated_jwt_when_extracting_authentication_then_an_authentication_with_user_name_and_user_id_is_created() {
        // Given
        var userId = 123456789;
        var userName = "user";
        var jwtAsMap = Map.<String, Object>of(
                JwtAccessTokenCustomizer.JAVA_USER_ID, userId,
                JwtAccessTokenCustomizer.JAVA_USER_NAME, userName);

        // When
        var oAuth2Authentication = sut.extractAuthentication(jwtAsMap);

        // Then
        assertThat(oAuth2Authentication).isNotNull();

        var userAuthentication = oAuth2Authentication.getUserAuthentication();
        assertThat(userAuthentication).isNotNull();
        assertThat(userAuthentication.getName()).isEqualTo(userName);

        var details = userAuthentication.getDetails();
        assertThat(details).isInstanceOf(UserAuthenticationDetails.class);

        var userAuthenticationDetails = (UserAuthenticationDetails) details;
        assertThat(userAuthenticationDetails.getUserId()).isEqualTo(userId);
    }

    @Test
    public void given_java_generated_jwt_without_user_id_when_extracting_authentication_then_an_authentication_with_user_name_is_created() {
        // Given
        var userName = "user";
        var jwtAsMap = Map.<String, Object>of(
                JwtAccessTokenCustomizer.JAVA_USER_NAME, userName);

        // When
        var oAuth2Authentication = sut.extractAuthentication(jwtAsMap);

        // Then
        assertThat(oAuth2Authentication).isNotNull();

        var userAuthentication = oAuth2Authentication.getUserAuthentication();
        assertThat(userAuthentication).isNotNull();
        assertThat(userAuthentication.getName()).isEqualTo(userName);

        assertThat(userAuthentication.getDetails()).isNull();
    }

    @Test
    public void given_php_generated_jwt_when_extracting_authentication_then_an_authentication_with_user_name_and_user_id_is_created() {
        // Given
        var userId = 123456789;
        var userName = "user";
        var phpDataStructure = Map.<String, Object>of(
                JwtAccessTokenCustomizer.PHP_USER_ID, userId,
                JwtAccessTokenCustomizer.PHP_USER_NAME, userName);
        var jwt = Map.of(JwtAccessTokenCustomizer.PHP_DATA_STRUCTURE, phpDataStructure);

        // When
        var oAuth2Authentication = sut.extractAuthentication(jwt);

        // Then
        assertThat(oAuth2Authentication).isNotNull();

        var userAuthentication = oAuth2Authentication.getUserAuthentication();
        assertThat(userAuthentication).isNotNull();
        assertThat(userAuthentication.getName()).isEqualTo(userName);

        var details = userAuthentication.getDetails();
        assertThat(details).isInstanceOf(UserAuthenticationDetails.class);

        var userAuthenticationDetails = (UserAuthenticationDetails) details;
        assertThat(userAuthenticationDetails.getUserId()).isEqualTo(123456789);
    }

    @Test
    public void given_php_generated_jwt_without_a_php_user_name_and_user_id_when_extracting_authentication_then_an_authentication_with_user_name_and_user_id_is_created() {
        // Given
        var userId = 123456789;
        var userName = "user";
        var phpDataStructure = Map.<String, Object>of();
        var jwt = Map.of(
                JwtAccessTokenCustomizer.JAVA_USER_ID, userId,
                JwtAccessTokenCustomizer.JAVA_USER_NAME, userName,
                JwtAccessTokenCustomizer.PHP_DATA_STRUCTURE, phpDataStructure);

        // When
        var oAuth2Authentication = sut.extractAuthentication(jwt);

        // Then
        assertThat(oAuth2Authentication).isNotNull();

        var userAuthentication = oAuth2Authentication.getUserAuthentication();
        assertThat(userAuthentication).isNotNull();
        assertThat(userAuthentication.getName()).isEqualTo(userName);

        var details = userAuthentication.getDetails();
        assertThat(details).isInstanceOf(UserAuthenticationDetails.class);

        var userAuthenticationDetails = (UserAuthenticationDetails) details;
        assertThat(userAuthenticationDetails.getUserId()).isEqualTo(123456789);
    }


    @Test
    public void when_configure_converter_then_converter_has_access_token_converter_set() {
        // When
        sut.configure(jwtAccessTokenConverter);

        // Then
        verify(jwtAccessTokenConverter).setAccessTokenConverter(sut);
    }
}