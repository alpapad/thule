package uk.co.serin.thule.spring.boot.starter.resourceserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import uk.co.serin.thule.resourceserver.utils.KeycloakJwtUtils;
import uk.co.serin.thule.spring.boot.starter.resourceserver.testservice.Application;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("itest")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResourceServerAutoConfigurationIntTest {
    public static final Set<GrantedAuthority> GRANTED_AUTHORITIES = Set.of(new SimpleGrantedAuthority("ROLE_PUBLIC"));
    public static final int USER_ID = 1234567890;
    public static final String USER_NAME = "user";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void given_an_access_control_request_method_http_header_when_making_an_options_request_then_access_should_be_granted() {
        // When
        webTestClient.options().uri("/hello")
                     .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name())
                     .header(HttpHeaders.ORIGIN, "http://localhost")
                     .exchange()

                     // Then
                     .expectStatus().isOk();
    }

    @Test
    void given_no_access_control_request_method_http_header_when_making_an_options_request_then_access_should_be_denied() {
        // When
        webTestClient.options().uri("/hello")
                     .exchange()

                     // Then
                     .expectStatus().isUnauthorized();
    }

    @Test
    void when_authenticated_then_access_should_be_granted() {
        // Given
        var jwt = KeycloakJwtUtils.createKeycloakJwt(USER_NAME, USER_ID, GRANTED_AUTHORITIES, "thule-test-service");

        // When
        webTestClient.get().uri("/hello")
                     .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue())
                     .exchange()

                     // Then
                     .expectStatus().isOk();
    }

    @Test
    void when_not_authenticated_then_access_should_be_denied() {
        // When
        var responseEntity = testRestTemplate.getForEntity("/hello", String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}