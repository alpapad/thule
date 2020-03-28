package uk.co.serin.thule.authentication.keycloak.resourceserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import uk.co.serin.thule.authentication.keycloak.KeycloakContainerInitializer;
import uk.co.serin.thule.authentication.keycloak.KeycloakRepository;
import uk.co.serin.thule.authentication.keycloak.resourceserver.testservice.Application;

@ActiveProfiles("itest")
@ContextConfiguration(initializers = KeycloakContainerInitializer.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeycloakResourceServerIntTest {
    @Autowired
    private KeycloakRepository keycloakRepository;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void given_a_micro_service_accounts_credentials_when_making_a_restful_call_to_another_micro_service_without_a_user_present_then_a_successful_response_is_returned() {
        //Given
        var thuleTestServiceClientSecret = keycloakRepository.getClientSecret(KeycloakContainerInitializer.THULE_TEST_SERVICE_CLIENT_ID);
        var jwt = keycloakRepository.getJwtFromKeycloakForService(KeycloakContainerInitializer.THULE_TEST_SERVICE_CLIENT_ID, thuleTestServiceClientSecret);

        //When
        webTestClient.get().uri("/hello")
                     .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                     .exchange()

                     // Then
                     .expectStatus().isOk()
                     .expectBody(String.class).isEqualTo("Hello World");
    }

    @Test
    public void given_a_users_credentials_when_making_a_restful_call_to_a_micro_service_then_a_successful_response_is_returned() {
        //Given
        var jwt = keycloakRepository.getJwtFromKeycloakForUser(
                KeycloakContainerInitializer.JOHN_DOE_USERNAME,
                KeycloakContainerInitializer.JOHN_DOE_PASSWORD,
                KeycloakContainerInitializer.THULE_WEBAPP_CLIENT_ID);

        //When
        webTestClient.get().uri("/hello")
                     .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                     .exchange()

                     // Then
                     .expectStatus().isOk()
                     .expectBody(String.class).isEqualTo("Hello World");
    }
}