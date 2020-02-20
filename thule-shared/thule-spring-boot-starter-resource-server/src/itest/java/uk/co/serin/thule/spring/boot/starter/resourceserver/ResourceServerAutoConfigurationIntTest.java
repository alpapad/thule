package uk.co.serin.thule.spring.boot.starter.resourceserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import uk.co.serin.thule.resourceserver.utils.JwtUtils;
import uk.co.serin.thule.spring.boot.starter.resourceserver.testservice.Application;

import java.util.Set;

@ActiveProfiles("itest")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceServerAutoConfigurationIntTest {
    public static final Set<GrantedAuthority> GRANTED_AUTHORITIES = Set.of(new SimpleGrantedAuthority("ROLE_PUBLIC"));
    public static final int USER_ID = 1234567890;
    public static final String USER_NAME = "user";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void given_an_access_control_request_method_http_header_when_making_an_options_request_then_access_should_be_granted() {
        // When
        webTestClient.options().uri("/hello")
                     .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name())
                     .header(HttpHeaders.ORIGIN, "http://localhost")
                     .exchange()

                     // Then
                     .expectStatus().isOk();
    }

    @Test
    public void given_no_access_control_request_method_http_header_when_making_an_options_request_then_access_should_be_denied() {
        // When
        webTestClient.options().uri("/hello")
                     .exchange()

                     // Then
                     .expectStatus().isUnauthorized();
    }

    @Test
    public void when_authenticated_then_access_should_be_granted() {
        // Given
        var jwt = JwtUtils.createKeycloakJwt(USER_NAME, USER_ID, GRANTED_AUTHORITIES, "thule-test-service");

        // When
        webTestClient.get().uri("/hello")
                     .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue())
                     .exchange()

                     // Then
                     .expectStatus().isOk();
    }

    @Test
    public void when_not_authenticated_then_access_should_be_denied() {
        // When
        webTestClient.get().uri("/hello")
                     .exchange()

                     // Then
                     .expectStatus().isUnauthorized();
    }
}