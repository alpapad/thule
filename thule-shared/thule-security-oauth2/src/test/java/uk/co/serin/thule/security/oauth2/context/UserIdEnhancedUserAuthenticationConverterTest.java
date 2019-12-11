package uk.co.serin.thule.security.oauth2.context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import uk.co.serin.thule.security.context.UserAuthenticationDetails;

import java.util.AbstractMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserIdEnhancedUserAuthenticationConverterTest {
    @Mock
    private Authentication authentication;
    @InjectMocks
    private UserIdEnhancedUserAuthenticationConverter sut;

    @Test
    public void given_authentication_when_converting_authentication_then_a_map_representation_with_user_name_and_user_id_is_created() {
        // Given
        var userId = 123456789L;
        var userName = "user";
        var userAuthenticationDetails = UserAuthenticationDetails.builder().userId(userId).build();
        given(authentication.getName()).willReturn(userName);
        given(authentication.getDetails()).willReturn(userAuthenticationDetails);

        // When
        var convertedAuthentication = (Map<String, Object>) sut.convertUserAuthentication(authentication);

        // Then
        assertThat(convertedAuthentication).isNotNull();
        assertThat(convertedAuthentication)
                .containsExactly(new AbstractMap.SimpleEntry<String, Object>(UserIdEnhancedUserAuthenticationConverter.JAVA_USER_ID, userId),
                        new AbstractMap.SimpleEntry<String, Object>(UserIdEnhancedUserAuthenticationConverter.JAVA_USER_NAME, userName));
    }

    @Test
    public void given_authentication_when_converting_authentication_then_a_map_representation_without_a_user_id_is_created() {
        // Given
        var userName = "user";
        given(authentication.getName()).willReturn(userName);

        // When
        var convertedAuthentication = (Map<String, Object>) sut.convertUserAuthentication(authentication);

        // Then
        assertThat(convertedAuthentication).isNotNull();
        assertThat(convertedAuthentication)
                .containsExactly(new AbstractMap.SimpleEntry<String, Object>(UserIdEnhancedUserAuthenticationConverter.JAVA_USER_NAME, userName));
    }

    @Test
    public void given_java_generated_jwt_when_extracting_authentication_then_an_authentication_with_user_name_and_user_id_is_created() {
        // Given
        var userId = 123456789;
        var userName = "user";
        var jwtClaims = Map.<String, Object>of(
                UserIdEnhancedUserAuthenticationConverter.JAVA_USER_ID, userId,
                UserIdEnhancedUserAuthenticationConverter.JAVA_USER_NAME, userName);

        // When
        var authentication = sut.extractAuthentication(jwtClaims);

        // Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo(userName);

        var details = authentication.getDetails();
        assertThat(details).isInstanceOf(UserAuthenticationDetails.class);

        var userAuthenticationDetails = (UserAuthenticationDetails) details;
        assertThat(userAuthenticationDetails.getUserId()).isEqualTo(userId);
    }

    @Test
    public void given_java_generated_jwt_without_user_id_when_extracting_authentication_then_an_authentication_with_user_name_is_created() {
        // Given
        var userName = "user";
        var jwtClaims = Map.<String, Object>of(
                UserIdEnhancedUserAuthenticationConverter.JAVA_USER_NAME, userName);

        // When
        var authentication = sut.extractAuthentication(jwtClaims);

        // Then
        assertThat(authentication).isNotNull();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo(userName);
        assertThat(authentication.getDetails()).isNull();
    }

    @Test
    public void given_php_generated_jwt_when_extracting_authentication_then_an_authentication_with_user_name_and_user_id_is_created() {
        // Given
        var userId = 123456789;
        var userName = "user";
        var phpDataStructure = Map.<String, Object>of(
                UserIdEnhancedUserAuthenticationConverter.PHP_USER_ID, userId,
                UserIdEnhancedUserAuthenticationConverter.PHP_USER_NAME, userName);
        var jwtClaims = Map.of(UserIdEnhancedUserAuthenticationConverter.PHP_DATA_STRUCTURE, phpDataStructure);

        // When
        var authentication = sut.extractAuthentication(jwtClaims);

        // Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo(userName);

        var details = authentication.getDetails();
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
        var jwtClaims = Map.of(
                UserIdEnhancedUserAuthenticationConverter.JAVA_USER_ID, userId,
                UserIdEnhancedUserAuthenticationConverter.JAVA_USER_NAME, userName,
                UserIdEnhancedUserAuthenticationConverter.PHP_DATA_STRUCTURE, phpDataStructure);

        // When
        var authentication = sut.extractAuthentication(jwtClaims);

        // Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo(userName);

        var details = authentication.getDetails();
        assertThat(details).isInstanceOf(UserAuthenticationDetails.class);

        var userAuthenticationDetails = (UserAuthenticationDetails) details;
        assertThat(userAuthenticationDetails.getUserId()).isEqualTo(123456789);
    }
}