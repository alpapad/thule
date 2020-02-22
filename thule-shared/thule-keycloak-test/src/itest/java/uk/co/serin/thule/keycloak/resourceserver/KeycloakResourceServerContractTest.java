package uk.co.serin.thule.keycloak.resourceserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;

import uk.co.serin.thule.keycloak.KeycloakContainerInitializer;
import uk.co.serin.thule.keycloak.KeycloakManager;
import uk.co.serin.thule.keycloak.resourceserver.testservice.Application;

@ActiveProfiles({"itest"})
@ContextConfiguration(initializers = KeycloakContainerInitializer.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeycloakResourceServerContractTest {
    private static final String TOKEN_ENDPOINT = "http://localhost:8080/auth/realms/thule-test/protocol/openid-connect/token";
    private static KeycloakManager keycloakManager = KeycloakManager.instance();
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void given_a_micro_service_accounts_credentials_when_making_a_restful_call_to_another_micro_service_without_a_user_present_then_a_successful_response_is_returned() {
        //Given
        var thuleTestServiceClientSecret = keycloakManager.getThuleTestServiceClientSecret();

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", KeycloakManager.THULE_TEST_SERVICE_CLIENT_ID);
        body.add("client_secret", thuleTestServiceClientSecret);

        var jwt = keycloakManager.getJwtFromKeycloak(TOKEN_ENDPOINT, body);

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
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("username", KeycloakManager.JOHN_DOE_USERNAME);
        body.add("password", KeycloakManager.JOHN_DOE_PASSWORD);
        body.add("client_id", KeycloakManager.THULE_WEBAPP_CLIENT_ID);

        var jwt = keycloakManager.getJwtFromKeycloak(TOKEN_ENDPOINT, body);

        //When
        webTestClient.get().uri("/hello")
                     .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                     .exchange()

                     // Then
                     .expectStatus().isOk()
                     .expectBody(String.class).isEqualTo("Hello World");
    }
}