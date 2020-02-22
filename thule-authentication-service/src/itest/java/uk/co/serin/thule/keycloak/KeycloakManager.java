package uk.co.serin.thule.keycloak;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KeycloakManager {
    private String keycloakAdminRealmsPath;
    private String keycloakAuthUrl;
    private String keycloakBase;
    private String realmName;
    private WebClient webClient;

    public KeycloakManager(String keycloakBase, String realmName) {
        this.keycloakBase = keycloakBase;
        this.realmName = realmName;
        this.keycloakAuthUrl = keycloakBase + "/auth";
        this.keycloakAdminRealmsPath = "/admin/realms/" + realmName;

        var adminJwt = getAdminJwtFromKeycloak();
        webClient = WebClient.builder().baseUrl(keycloakAuthUrl).defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwt).build();
    }

    private String getAdminJwtFromKeycloak() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("username", "admin");
        body.add("password", "admin");
        body.add("client_id", "admin-cli");

        return getJwtFromKeycloak(keycloakAuthUrl + "/realms/master/protocol/openid-connect/token", body);
    }

    public String getJwtFromKeycloak(String url, LinkedMultiValueMap<String, String> body) {
        var token = WebClient.builder().baseUrl(url).build()
                             .post().bodyValue(body)
                             .retrieve()
                             .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                             }).block();

        return token.get("access_token").toString();
    }

    public void createPublicClient(String clientId) {
        var adminUrl = keycloakAdminRealmsPath + "/clients";

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
                "redirectUris", new String[]{keycloakBase},
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
        var adminUrl = keycloakAdminRealmsPath + "/clients/{clientId}/roles";

        // Delete
        var roles = webClient.get().uri(adminUrl, clientId).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(roles).filter(realm -> realm.get("name").equals(roleName))
              .forEach(role -> webClient.delete().uri(adminUrl + "/{name}", clientId, role.get("name")).exchange().block());

        // Create
        var role = Map.of("name", roleName);
        webClient.post().uri(adminUrl, clientId).bodyValue(role).exchange().block();
    }

    public void createServiceClient(String clientId) {
        var adminUrl = keycloakAdminRealmsPath + "/clients";

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
                "redirectUris", new String[]{keycloakBase},
                "serviceAccountsEnabled", Boolean.TRUE,
                "standardFlowEnabled", Boolean.TRUE);
        webClient.post().uri(adminUrl).bodyValue(client).exchange().block();
    }

    public String createUser(String userName, String password) {
        var adminUrl = keycloakAdminRealmsPath + "/users";

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
                "firstName", "John",
                "enabled", Boolean.TRUE,
                "lastName", "Doe",
                "username", userName);
        var clientResponse = webClient.post().uri(adminUrl).bodyValue(user).exchange().block();
        var url = clientResponse.headers().header(HttpHeaders.LOCATION).stream().findFirst().orElseThrow();

        // Create role mappings for user
        var uriPathElements = url.split("/");
        return uriPathElements[uriPathElements.length - 1];
    }

    public void createUserRoleMapping(String userId, String clientId, String roleName) {
        var adminUrl = keycloakAdminRealmsPath + "/clients/{clientId}/roles/{roleName}";
        var role = webClient.get().uri(adminUrl, clientId, roleName).retrieve().bodyToMono(Map.class).block();

        adminUrl = keycloakAdminRealmsPath + "/users/{userId}/role-mappings/clients/{clientId}";
        webClient.post().uri(adminUrl, userId, clientId).bodyValue(List.of(role)).exchange().block();
    }

    public String getClientSecret(String clientId) {
        return webClient.get().uri(keycloakAdminRealmsPath + "/clients/{clientId}/client-secret", clientId).retrieve()
                        .bodyToMono(Map.class).block().get("value").toString();
    }
}