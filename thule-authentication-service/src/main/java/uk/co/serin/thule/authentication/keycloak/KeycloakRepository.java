package uk.co.serin.thule.authentication.keycloak;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import uk.co.serin.thule.utils.trace.TracePublicMethods;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;

@SuppressWarnings("squid:S1192") // Suppress String literals should not be duplicated
@TracePublicMethods
public class KeycloakRepository {
    private static final String ADMIN_REALMS = "/admin/realms/";
    private String adminRealmsPath = ADMIN_REALMS;
    private URI keycloakBaseUrl;
    private KeycloakProperties keycloakProperties;
    private String realmName;
    private SslContextBuilder sslContextBuilder = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE);
    private WebClient webClientWithAdminBearerAuth = WebClient.create();
    private WebClient webClientWithoutAdminBearerAuth = WebClient.create();

    public KeycloakRepository(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    public void createPublicClient(String clientId) {
        var adminUrl = adminRealmsPath + "/clients";
        var redirectUri = keycloakBaseUrl.getScheme() + "://" + keycloakBaseUrl.getAuthority() + "/*";

        // Delete
        var clients = webClientWithAdminBearerAuth.get().uri(adminUrl + "?clientId={clientId}", clientId).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(clients).filter(realm -> realm.get("id").equals(clientId))
              .forEach(client -> webClientWithAdminBearerAuth.delete().uri(adminUrl + "/{id}", client.get("id")).exchange().block());

        // Create
        var client = Map.of(
                "directAccessGrantsEnabled", true,
                "enabled", true,
                "id", clientId,
                "name", clientId,
                "publicClient", true,
                "redirectUris", new String[]{redirectUri},
                "standardFlowEnabled", true);
        webClientWithAdminBearerAuth.post().uri(adminUrl).bodyValue(client).exchange().block();
    }

    public void createRealm() {
        var adminUrl = "/admin/realms";

        // Delete
        var realms = webClientWithAdminBearerAuth.get().uri(adminUrl).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(realms).filter(realm -> realm.get("realm").equals(realmName))
              .forEach(realm -> webClientWithAdminBearerAuth.delete().uri(adminUrl + "/{realm}", realm.get("realm")).exchange().block());

        // Create
        var realm = Map.of(
                "realm", realmName,
                "enabled", true);
        webClientWithAdminBearerAuth.post().uri(adminUrl).bodyValue(realm).exchange().block();
    }

    public void createRoleForClient(String roleName, String clientId) {
        var adminUrl = adminRealmsPath + "/clients/{clientId}/roles";

        // Delete
        var roles = webClientWithAdminBearerAuth.get().uri(adminUrl, clientId).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(roles).filter(realm -> realm.get("name").equals(roleName))
              .forEach(role -> webClientWithAdminBearerAuth.delete().uri(adminUrl + "/{name}", clientId, role.get("name")).exchange().block());

        // Create
        var role = Map.of("name", roleName);
        webClientWithAdminBearerAuth.post().uri(adminUrl, clientId).bodyValue(role).exchange().block();
    }

    public void createServiceClient(String clientId) {
        var adminUrl = adminRealmsPath + "/clients";
        var redirectUri = keycloakBaseUrl.getScheme() + "://" + keycloakBaseUrl.getAuthority() + "/*";

        // Delete
        var clients = webClientWithAdminBearerAuth.get().uri(adminUrl + "?clientId={clientId}", clientId).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(clients).filter(realm -> realm.get("id").equals(clientId))
              .forEach(client -> webClientWithAdminBearerAuth.delete().uri(adminUrl + "/{id}", client.get("id")).exchange().block());

        // Create
        var client = Map.of(
                "authorizationServicesEnabled", true,
                "directAccessGrantsEnabled", true,
                "enabled", true,
                "id", clientId,
                "name", clientId,
                "redirectUris", new String[]{redirectUri},
                "serviceAccountsEnabled", true,
                "standardFlowEnabled", true);
        webClientWithAdminBearerAuth.post().uri(adminUrl).bodyValue(client).exchange().block();
    }

    public String createUser(String userName, String password, String firstName, String lastName) {
        var adminUrl = adminRealmsPath + "/users";

        // Delete
        var users = webClientWithAdminBearerAuth.get().uri(adminUrl + "?search={username}", userName).retrieve().bodyToMono(Map[].class).block();
        Arrays.stream(users).filter(realm -> realm.get("username").equals(userName))
              .forEach(user -> webClientWithAdminBearerAuth.delete().uri(adminUrl + "/{id}", user.get("username")).exchange().block());

        // Create
        var credential = Map.of(
                "type", "password",
                "temporary", false,
                "value", password);
        var user = Map.of(
                "credentials", new Map[]{credential},
                "firstName", firstName,
                "enabled", true,
                "lastName", lastName,
                "username", userName);
        var clientResponse = webClientWithAdminBearerAuth.post().uri(adminUrl).bodyValue(user).exchange().block();
        var url = clientResponse.headers().header(HttpHeaders.LOCATION).stream().findFirst().orElseThrow();

        // Create role mappings for user
        var uriPathElements = url.split("/");
        return uriPathElements[uriPathElements.length - 1];
    }

    public void createUserRoleMapping(String userId, String clientId, String roleName) {
        var adminUrl = adminRealmsPath + "/clients/{clientId}/roles/{roleName}";
        var role = webClientWithAdminBearerAuth.get().uri(adminUrl, clientId, roleName).retrieve().bodyToMono(Map.class).block();

        adminUrl = adminRealmsPath + "/users/{userId}/role-mappings/clients/{clientId}";
        webClientWithAdminBearerAuth.post().uri(adminUrl, userId, clientId).bodyValue(List.of(role)).exchange().block();
    }

    public String getClientSecret(String clientId) {
        return webClientWithAdminBearerAuth.get().uri(adminRealmsPath + "/clients/{clientId}/client-secret", clientId).retrieve()
                                           .bodyToMono(Map.class).block().get("value").toString();
    }

    public String getJwtFromKeycloakForService(String clientId, String clientSecret) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        return getJwtFromKeycloak(realmName, body);
    }

    private String getJwtFromKeycloak(String realmName, LinkedMultiValueMap<String, String> body) {
        var token = webClientWithoutAdminBearerAuth.post().uri("/realms/{realmName}/protocol/openid-connect/token", realmName).bodyValue(body)
                                                   .retrieve()
                                                   .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                                                   }).block();

        return token.get("access_token").toString();
    }

    public String getJwtFromKeycloakForUser(String username, String password, String clientId) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        body.add("client_id", clientId);

        return getJwtFromKeycloak(realmName, body);
    }

    @PostConstruct
    public void init() {
        keycloakBaseUrl = keycloakProperties.getBaseUrl();
        realmName = keycloakProperties.getRealm();
        adminRealmsPath = ADMIN_REALMS + realmName;

        webClientWithoutAdminBearerAuth =
                webClientWithoutAdminBearerAuth.mutate().clientConnector(createHttpClientTrustingAllSslCertificates()).baseUrl(keycloakBaseUrl + "/auth")
                                               .build();

        var adminJwt = getJwtFromKeycloakForAdminUser();
        webClientWithAdminBearerAuth =
                webClientWithoutAdminBearerAuth.mutate().clientConnector(createHttpClientTrustingAllSslCertificates())
                                               .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwt).build();
    }

    private ReactorClientHttpConnector createHttpClientTrustingAllSslCertificates() {
        try {
            var sslContext = sslContextBuilder.build();
            var httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
            return new ReactorClientHttpConnector(httpClient);
        } catch (SSLException e) {
            throw new SecurityException(e);
        }
    }

    private String getJwtFromKeycloakForAdminUser() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("username", keycloakProperties.getAdmin().getUsername());
        body.add("password", keycloakProperties.getAdmin().getPassword());
        body.add("client_id", keycloakProperties.getAdmin().getClientId());

        return getJwtFromKeycloak("master", body);
    }
}