package uk.co.serin.thule.keycloak;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.time.Duration;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

public class KeycloakContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static final String JOHN_DOE_PASSWORD = "password";
    public static final String JOHN_DOE_USERNAME = "john.doe@thule.co.uk";
    public static final String KEYCLOAK_BASE_URL = "http://localhost:8080";
    public static final String KEYCLOAK_AUTH_URL = KEYCLOAK_BASE_URL + "/auth";
    public static final String THULE_REALM_NAME = "thule-test";
    public static final String KEYCLOAK_TOKEN_ENDPOINT =
            KeycloakContainerInitializer.KEYCLOAK_AUTH_URL + "/realms/" + KeycloakContainerInitializer.THULE_REALM_NAME + "/protocol/openid-connect/token";
    public static final String THULE_ROLE_NAME = "USER";
    public static final String THULE_TEST_SERVICE_CLIENT_ID = "thule-test-service";
    public static final String THULE_WEBAPP_CLIENT_ID = "thule-webapp";
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/itest/docker/thule-authentication-service-tests/docker-compose.yml");
    private static boolean initialized;
    private WebClient webClient = WebClient.builder().baseUrl(KEYCLOAK_AUTH_URL).build();

    @Override
    public synchronized void initialize(ConfigurableApplicationContext applicationContext) {
        if (!initialized) {
            startKeycloak();
            createKeycloakResources();
            initialized = true;
        }
    }

    private void startKeycloak() {
        try {
            DOCKER_COMPOSE.downAndUp();
            // Wait until keycloak is up by ensuring the home page is available
            given().ignoreExceptions().pollInterval(fibonacci()).
                    await().timeout(Duration.ofMinutes(5)).
                           untilAsserted(() -> {
                               webClient.get().retrieve().bodyToMono(String.class).block();
                           });
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void createKeycloakResources() {
        var keycloakManager = new KeycloakManager(KEYCLOAK_BASE_URL, THULE_REALM_NAME);

        keycloakManager.createRealm();

        keycloakManager.createServiceClient(THULE_TEST_SERVICE_CLIENT_ID);
        keycloakManager.createRoleForClient(THULE_ROLE_NAME, THULE_TEST_SERVICE_CLIENT_ID);

        keycloakManager.createPublicClient(THULE_WEBAPP_CLIENT_ID);
        keycloakManager.createRoleForClient(THULE_ROLE_NAME, THULE_WEBAPP_CLIENT_ID);

        var userId = keycloakManager.createUser(JOHN_DOE_USERNAME, JOHN_DOE_PASSWORD);
        keycloakManager.createUserRoleMapping(userId, THULE_TEST_SERVICE_CLIENT_ID, THULE_ROLE_NAME);
        keycloakManager.createUserRoleMapping(userId, THULE_WEBAPP_CLIENT_ID, THULE_ROLE_NAME);
    }
}