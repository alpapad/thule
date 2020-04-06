package uk.co.serin.thule.authentication.resourceserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import uk.co.serin.thule.authentication.KeycloakBaseIntTest;
import uk.co.serin.thule.authentication.resourceserver.testservice.Application;

@ActiveProfiles("itest")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeycloakResourceServerIntTest extends KeycloakBaseIntTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void given_a_micro_service_accounts_credentials_when_making_a_restful_call_to_another_micro_service_without_a_user_present_then_a_successful_response_is_returned() {
        // Given
        var thuleTestServiceClientSecret = getKeycloakRepository().getClientSecret(KeycloakBaseIntTest.THULE_TEST_SERVICE_CLIENT_ID);
        var jwt = getKeycloakRepository().getJwtFromKeycloakForService(KeycloakBaseIntTest.THULE_TEST_SERVICE_CLIENT_ID, thuleTestServiceClientSecret);

        // When
        webTestClient
                .get().uri("/hello")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .exchange()

                // Then
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello World");
    }

    @Test
    public void given_a_users_credentials_when_making_a_restful_call_to_a_micro_service_then_a_successful_response_is_returned() {
        // Given
        var jwt = getKeycloakRepository().getJwtFromKeycloakForUser(
                KeycloakBaseIntTest.JOHN_DOE_USERNAME,
                KeycloakBaseIntTest.JOHN_DOE_PASSWORD,
                KeycloakBaseIntTest.THULE_WEBAPP_CLIENT_ID);

        // When
        webTestClient
                .get().uri("/hello")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .exchange()

                // Then
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello World");
    }
}