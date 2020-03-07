package uk.co.serin.thule.authentication.keycloak;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import uk.co.serin.thule.utils.trace.TracePublicMethods;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@TracePublicMethods
public class KeycloakRepository {
    private String adminRealmsPath;
    private String keycloakBaseUrl;
    private KeycloakProperties keycloakProperties;
    private String realmName;
    private WebClient webClient;

    public KeycloakRepository(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
        this.keycloakBaseUrl = keycloakProperties.getBaseUrl();
        this.realmName = keycloakProperties.getRealm();
        this.adminRealmsPath = "/admin/realms/" + realmName;

        var adminJwt = getJwtFromKeycloakForAdminUser();
        webClient = WebClient.builder().baseUrl(keycloakBaseUrl + "/auth").defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwt).build();
    }

    private String getJwtFromKeycloakForAdminUser() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("username", keycloakProperties.getAdmin().getUsername());
        body.add("password", keycloakProperties.getAdmin().getPassword());
        body.add("client_id", keycloakProperties.getAdmin().getClientId());

        return getJwtFromKeycloak("master", body);
    }

    private String getJwtFromKeycloak(String realmName, LinkedMultiValueMap<String, String> body) {
        var token = WebClient.builder().baseUrl(keycloakBaseUrl).build()
                             .post().uri("/auth/realms/{realmName}/protocol/openid-connect/token", realmName).bodyValue(body)
                             .retrieve()
                             .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                             }).block();

        return token.get("access_token").toString();
    }

    public void createPublicClient(String clientId) {
        var adminUrl = adminRealmsPath + "/clients";

        // Delete
        var clients = webClient.get().uri(adminUrl + "?clientId={clientId}", clientId).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(clients).filter(realm -> realm.get("id").equals(clientId))
              .forEach(client -> webClient.delete().uri(adminUrl + "/{id}", client.get("id")).exchange().block());

        // Create
        var client = Map.of(
                "directAccessGrantsEnabled", Boolean.TRUE,
                "enabled", Boolean.TRUE,
                "id", clientId,
                "name", clientId,
                "publicClient", Boolean.TRUE,
                "redirectUris", new String[]{keycloakBaseUrl},
                "standardFlowEnabled", Boolean.TRUE);
        webClient.post().uri(adminUrl).bodyValue(client).exchange().block();
    }

    public void createRealm() {
        var adminUrl = "/admin/realms";

        // Delete
        var realms = webClient.get().uri(adminUrl).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(realms).filter(realm -> realm.get("realm").equals(realmName))
              .forEach(realm -> webClient.delete().uri(adminUrl + "/{realm}", realm.get("realm")).exchange().block());

        // Create
        var realm = Map.of(
                "realm", realmName,
                "enabled", Boolean.TRUE);
        webClient.post().uri(adminUrl).bodyValue(realm).exchange().block();
    }

    public void createRoleForClient(String roleName, String clientId) {
        var adminUrl = adminRealmsPath + "/clients/{clientId}/roles";

        // Delete
        var roles = webClient.get().uri(adminUrl, clientId).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(roles).filter(realm -> realm.get("name").equals(roleName))
              .forEach(role -> webClient.delete().uri(adminUrl + "/{name}", clientId, role.get("name")).exchange().block());

        // Create
        var role = Map.of("name", roleName);
        webClient.post().uri(adminUrl, clientId).bodyValue(role).exchange().block();
    }

    public void createServiceClient(String clientId) {
        var adminUrl = adminRealmsPath + "/clients";

        // Delete
        var clients = webClient.get().uri(adminUrl + "?clientId={clientId}", clientId).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(clients).filter(realm -> realm.get("id").equals(clientId))
              .forEach(client -> webClient.delete().uri(adminUrl + "/{id}", client.get("id")).exchange().block());

        // Create
        var client = Map.of(
                "authorizationServicesEnabled", Boolean.TRUE,
                "directAccessGrantsEnabled", Boolean.TRUE,
                "enabled", Boolean.TRUE,
                "id", clientId,
                "name", clientId,
                "redirectUris", new String[]{keycloakBaseUrl},
                "serviceAccountsEnabled", Boolean.TRUE,
                "standardFlowEnabled", Boolean.TRUE);
        webClient.post().uri(adminUrl).bodyValue(client).exchange().block();
    }

    public String createUser(String userName, String password, String firstName, String lastName) {
        var adminUrl = adminRealmsPath + "/users";

        // Delete
        var users = webClient.get().uri(adminUrl + "?search={username}", userName).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(users).filter(realm -> realm.get("username").equals(userName))
              .forEach(user -> webClient.delete().uri(adminUrl + "/{id}", user.get("username")).exchange().block());

        // Create
        var credential = Map.of(
                "type", "password",
                "temporary", Boolean.FALSE,
                "value", password);
        var user = Map.of(
                "credentials", new Map[]{credential},
                "firstName", firstName,
                "enabled", Boolean.TRUE,
                "lastName", lastName,
                "username", userName);
        var clientResponse = webClient.post().uri(adminUrl).bodyValue(user).exchange().block();
        var url = clientResponse.headers().header(HttpHeaders.LOCATION).stream().findFirst().orElseThrow();

        // Create role mappings for user
        var uriPathElements = url.split("/");
        return uriPathElements[uriPathElements.length - 1];
    }

    public void createUserRoleMapping(String userId, String clientId, String roleName) {
        var adminUrl = adminRealmsPath + "/clients/{clientId}/roles/{roleName}";
        var role = webClient.get().uri(adminUrl, clientId, roleName).retrieve().bodyToMono(Map.class).block();

        adminUrl = adminRealmsPath + "/users/{userId}/role-mappings/clients/{clientId}";
        webClient.post().uri(adminUrl, userId, clientId).bodyValue(List.of(role)).exchange().block();
    }

    public String getClientSecret(String clientId) {
        return webClient.get().uri(adminRealmsPath + "/clients/{clientId}/client-secret", clientId).retrieve()
                        .bodyToMono(Map.class).block().get("value").toString();
    }

    public String getJwtFromKeycloakForService(String clientId, String clientSecret) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        return getJwtFromKeycloak(realmName, body);
    }

    public String getJwtFromKeycloakForUser(String username, String password, String clientId) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        body.add("client_id", clientId);

        return getJwtFromKeycloak(realmName, body);
    }
}