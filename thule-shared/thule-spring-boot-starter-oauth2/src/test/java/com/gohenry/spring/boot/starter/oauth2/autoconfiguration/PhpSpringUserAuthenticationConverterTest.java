package com.gohenry.spring.boot.starter.oauth2.autoconfiguration;

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
        PhpSpringUserAuthenticationConverter javaPhpAuthenticationConverter = new PhpSpringUserAuthenticationConverter();

        Map<String, Object> javaTokenMap = new HashMap<>();

        javaTokenMap.put("user_name", "username");
        javaTokenMap.put("authorities", Collections.singletonList("grantedAuthority"));


        //When
        Authentication authentication = javaPhpAuthenticationConverter.extractAuthentication(javaTokenMap);

        //Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("username");
    }

    @Test
    public void when_presented_php_based_map_then_convert_to_java_based_authentication() {
        //Given
        PhpSpringUserAuthenticationConverter javaPhpAuthenticationConverter = new PhpSpringUserAuthenticationConverter();

        Map<String, Object> phpTokenMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("userId", "username");
        data.put("refreshAfter", Long.MAX_VALUE);

        phpTokenMap.put("data", data);

        //When
        Authentication authentication = javaPhpAuthenticationConverter.extractAuthentication(phpTokenMap);

        //Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("username");
    }
}
