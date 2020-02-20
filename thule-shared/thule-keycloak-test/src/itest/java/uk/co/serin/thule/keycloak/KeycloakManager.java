package uk.co.serin.thule.keycloak;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

public class KeycloakManager {
    public static final String THULE_TEST_SERVICE_CLIENT_ID = "thule-test-service";
    public static final String THULE_WEBAPP_CLIENT_ID = "thule-webapp";
    public static final String JOHN_DOE_PASSWORD = "password";
    public static final String JOHN_DOE_USERNAME = "john.doe@thule.co.uk";
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/itest/docker/docker-compose.yml");
    private static final String KEYCLOAK_BASE = "http://localhost:8080";
    private static final String KEYCLOAK_BASE_URL = "http://localhost:8080/auth";
    private static final String KEYCLOAK_ROLE_NAME = "USER";
    private static final String KEYCLOAK_TEST_REALM = "thule-test";
    private static final String KEYCLOAK_TEST_REALM_PATH = "/admin/realms/" + KEYCLOAK_TEST_REALM;
    private static boolean keycloakIsInitialized;
    private static KeycloakManager keycloakManager = new KeycloakManager();
    private WebClient webClient = WebClient.builder().baseUrl(KEYCLOAK_BASE_URL).build();

    private KeycloakManager() {
    }

    public static synchronized KeycloakManager instance() {
        return keycloakManager;
    }

    public String getThuleTestServiceClientSecret() {
        var adminJwt = getAdminJwtFromKeycloak();
        webClient = webClient.mutate().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwt).build();

        return webClient.get().uri(KEYCLOAK_TEST_REALM_PATH + "/clients/{clientId}/client-secret", THULE_TEST_SERVICE_CLIENT_ID).retrieve()
                        .bodyToMono(Map.class).block().get("value").toString();
    }

    private String getAdminJwtFromKeycloak() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("username", "admin");
        body.add("password", "admin");
        body.add("client_id", "admin-cli");

        return getJwtFromKeycloak(KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/token", body);
    }

    public String getJwtFromKeycloak(String url, LinkedMultiValueMap<String, String> body) {
        var token = WebClient.builder().baseUrl(url).build()
                             .post().bodyValue(body)
                             .retrieve()
                             .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                             }).block();

        return token.get("access_token").toString();
    }

    public synchronized void shutdown() {
        if (keycloakIsInitialized) {
            try {
                DOCKER_COMPOSE.down();
                keycloakIsInitialized = false;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public synchronized void startUp() {
        if (!keycloakIsInitialized) {
            try {
                DOCKER_COMPOSE.downAndUp();
                // Wait until keycloak is up by ensuring the home page is available
                given().ignoreExceptions().pollInterval(fibonacci()).
                        await().timeout(Duration.ofMinutes(5)).
                               untilAsserted(() -> {
                                   webClient.get().retrieve().bodyToMono(String.class).block();
                               });
                keycloakManager.setupKeycloak();
                keycloakIsInitialized = true;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private void setupKeycloak() {
        var adminJwt = getAdminJwtFromKeycloak();
        webClient = webClient.mutate().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwt).build();

        createRealm();
        createThuleTestServiceClient();
        createThuleWebappClient();
        createUser();
    }

    private void createRealm() {
        var adminUrl = "/admin/realms";

        // Delete
        var realms = webClient.get().uri(adminUrl).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(realms).filter(realm -> realm.get("realm").equals(KEYCLOAK_TEST_REALM))
              .forEach(realm -> webClient.delete().uri(adminUrl + "/{realm}", realm.get("realm")).exchange().block());

        // Create
        var realm = Map.of(
                "realm", KEYCLOAK_TEST_REALM,
                "enabled", Boolean.TRUE);
        webClient.post().uri(adminUrl).bodyValue(realm).exchange().block();
    }

    private void createThuleTestServiceClient() {
        var adminUrl = KEYCLOAK_TEST_REALM_PATH + "/clients";

        // Delete
        var clients = webClient.get().uri(adminUrl + "?clientId={clientId}", THULE_TEST_SERVICE_CLIENT_ID).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(clients).filter(realm -> realm.get("id").equals(THULE_TEST_SERVICE_CLIENT_ID))
              .forEach(client -> webClient.delete().uri(adminUrl + "/{id}", client.get("id")).exchange().block());

        // Create
        var client = Map.of(
                "authorizationServicesEnabled", Boolean.TRUE,
                "directAccessGrantsEnabled", Boolean.TRUE,
                "enabled", Boolean.TRUE,
                "id", THULE_TEST_SERVICE_CLIENT_ID,
                "name", THULE_TEST_SERVICE_CLIENT_ID,
                "redirectUris", new String[]{KEYCLOAK_BASE},
                "serviceAccountsEnabled", Boolean.TRUE,
                "standardFlowEnabled", Boolean.TRUE);
        webClient.post().uri(adminUrl).bodyValue(client).exchange().block();

        // Create role for client
        createRole(THULE_TEST_SERVICE_CLIENT_ID);
    }

    private void createThuleWebappClient() {
        var adminUrl = KEYCLOAK_TEST_REALM_PATH + "/clients";

        // Delete
        var clients = webClient.get().uri(adminUrl + "?clientId={clientId}", THULE_WEBAPP_CLIENT_ID).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(clients).filter(realm -> realm.get("id").equals(THULE_WEBAPP_CLIENT_ID))
              .forEach(client -> webClient.delete().uri(adminUrl + "/{id}", client.get("id")).exchange().block());

        // Create
        var client = Map.of(
                "directAccessGrantsEnabled", Boolean.TRUE,
                "enabled", Boolean.TRUE,
                "id", THULE_WEBAPP_CLIENT_ID,
                "name", THULE_WEBAPP_CLIENT_ID,
                "publicClient", Boolean.TRUE,
                "redirectUris", new String[]{KEYCLOAK_BASE},
                "standardFlowEnabled", Boolean.TRUE);
        webClient.post().uri(adminUrl).bodyValue(client).exchange().block();

        // Create role for client
        createRole(THULE_WEBAPP_CLIENT_ID);
    }

    private void createUser() {
        var adminUrl = KEYCLOAK_TEST_REALM_PATH + "/users";

        // Delete
        var users = webClient.get().uri(adminUrl + "?search={username}", JOHN_DOE_USERNAME).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(users).filter(realm -> realm.get("username").equals(JOHN_DOE_USERNAME))
              .forEach(user -> webClient.delete().uri(adminUrl + "/{id}", user.get("username")).exchange().block());

        // Create
        var credential = Map.of(
                "type", "password",
                "temporary", Boolean.FALSE,
                "value", JOHN_DOE_PASSWORD);
        var user = Map.of(
                "credentials", new Map[]{credential},
                "firstName", "John",
                "enabled", Boolean.TRUE,
                "lastName", "Doe",
                "username", JOHN_DOE_USERNAME);
        var clientResponse = webClient.post().uri(adminUrl).bodyValue(user).exchange().block();
        var url = clientResponse.headers().header(HttpHeaders.LOCATION).stream().findFirst().orElseThrow();

        // Create role mappings for user
        var uriPathElements = url.split("/");
        var userId = uriPathElements[uriPathElements.length - 1];
        createUserRoleMapping(userId, THULE_TEST_SERVICE_CLIENT_ID, KEYCLOAK_ROLE_NAME);
        createUserRoleMapping(userId, THULE_WEBAPP_CLIENT_ID, KEYCLOAK_ROLE_NAME);
    }

    private void createRole(String clientId) {
        var adminUrl = KEYCLOAK_TEST_REALM_PATH + "/clients/{clientId}/roles";

        // Delete
        var roles = webClient.get().uri(adminUrl, clientId).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(roles).filter(realm -> realm.get("name").equals(KEYCLOAK_ROLE_NAME))
              .forEach(role -> webClient.delete().uri(adminUrl + "/{name}", clientId, role.get("name")).exchange().block());

        // Create
        var role = Map.of("name", KEYCLOAK_ROLE_NAME);
        webClient.post().uri(adminUrl, clientId).bodyValue(role).exchange().block();
    }

    private void createUserRoleMapping(String userId, String clientId, String roleName) {
        var adminUrl = KEYCLOAK_TEST_REALM_PATH + "/clients/{clientId}/roles/{roleName}";
        var role = webClient.get().uri(adminUrl, clientId, roleName).retrieve().bodyToMono(Map.class).block();

        adminUrl = KEYCLOAK_TEST_REALM_PATH + "/users/{userId}/role-mappings/clients/{clientId}";
        webClient.post().uri(adminUrl, userId, clientId).bodyValue(List.of(role)).exchange().block();
    }
}