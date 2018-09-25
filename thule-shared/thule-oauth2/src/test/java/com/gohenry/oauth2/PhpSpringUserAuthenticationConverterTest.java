package com.gohenry.oauth2;

import org.junit.Test;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PhpSpringUserAuthenticationConverterTest {

    @Test
    public void when_presented_java_based_map_then_return_java_based_authentication() {
        //Given
        long expectedUserId = 1234567890;
        PhpSpringUserAuthenticationConverter sut = new PhpSpringUserAuthenticationConverter();
        Map<String, Object> javaTokenMap = new HashMap<>();

        javaTokenMap.put("user_name", "username");
        javaTokenMap.put("authorities", Collections.singletonList("grantedAuthority"));
        javaTokenMap.put("user_id", expectedUserId);

        //When
        Authentication authentication = sut.extractAuthentication(javaTokenMap);

        //Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("username");
        UserAuthenticationDetails actualUserAuthenticationDetails = (UserAuthenticationDetails) authentication.getDetails();
        assertThat(actualUserAuthenticationDetails.getUserId()).isEqualTo(expectedUserId);
    }

    @Test(expected = UserIdNotFoundException.class)
    public void when_php_map_does_not_contain_user_id_then_user_id_not_found_exception_is_thrown() {
        //Given
        PhpSpringUserAuthenticationConverter sut = new PhpSpringUserAuthenticationConverter();

        Map<String, Object> phpTokenMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        phpTokenMap.put("data", data);

        //When
        Authentication authentication = sut.extractAuthentication(phpTokenMap);

        //Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("unavailable");
    }

    @Test
    public void when_presented_php_based_map_with_user_name_available_then_convert_to_java_based_authentication() {
        //Given
        final long expectedDetails = 1144563424569673L;
        PhpSpringUserAuthenticationConverter sut = new PhpSpringUserAuthenticationConverter();

        Map<String, Object> phpTokenMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("user_id", expectedDetails);
        data.put("userName", "userName");

        phpTokenMap.put("data", data);

        //When
        Authentication authentication = sut.extractAuthentication(phpTokenMap);

        //Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("userName");

        UserAuthenticationDetails userAuthenticationDetails = (UserAuthenticationDetails) authentication.getDetails();
        assertThat(userAuthenticationDetails.getUserId()).isEqualTo(expectedDetails);
    }

    @Test
    public void when_presented_php_based_map_with_user_name_unavailable_then_convert_to_java_based_authentication() {
        //Given
        PhpSpringUserAuthenticationConverter sut = new PhpSpringUserAuthenticationConverter();

        Map<String, Object> phpTokenMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("user_id", 1234567890);
        phpTokenMap.put("data", data);

        //When
        Authentication authentication = sut.extractAuthentication(phpTokenMap);

        //Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("unavailable");
    }
}
