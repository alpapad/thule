package uk.co.serin.thule.authentication.keycloak;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class KeycloakRepositoryTest {
    protected static final String TEST_CLIENT_SECRET = "test-client-secret";
    private static final String TEST_CLIENT_ID = "thule-test-service";
    private static final String TEST_FIRST_NAME = "test-first-name";
    private static final String TEST_ID = "12345678";
    private static final String TEST_JWT = "test-jwt";
    private static final String TEST_LAST_NAME = "test-last-name";
    private static final String TEST_PASSWORD = "test-password";
    private static final String TEST_REALM = "test-realm";
    private static final String TEST_ROLE = "test-role";
    private static final String TEST_USERNAME = "test@serin-consultancy.co.uk";
    @Mock
    private KeycloakProperties.Admin admin;
    @Mock
    private KeycloakProperties keycloakProperties;
    private ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    private KeycloakRepository sut;
    private WebClient webClient;

    @Test
    public void given_a_clientid_when_creating_a_public_client_then_keycloak_post_clients_is_executed() throws JsonProcessingException {
        // Given
        var clients = new Map[]{Map.of("id", TEST_CLIENT_ID)};
        var responseBody = objectMapper.writeValueAsString(clients);

        var keycloakPostWasExecuted = mockWebClientResponses(responseBody);
        ReflectionTestUtils.setField(sut, "webClientWithAdminBearerAuth", webClient);
        ReflectionTestUtils.setField(sut, "keycloakBaseUrl", URI.create("http://localhost"));

        // When
        sut.createPublicClient(TEST_CLIENT_ID);

        //Then
        assertThat(keycloakPostWasExecuted).isTrue();
    }

    private AtomicBoolean mockWebClientResponses(String... responseBodies) {
        final var keycloakPostWasExecuted = new AtomicBoolean();

        for (var responseBody : responseBodies) {
            webClient = WebClient.builder().exchangeFunction(request -> {
                var clientResponse =
                        ClientResponse.create(HttpStatus.OK)
                                      .header(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE)
                                      .header(HttpHeaders.LOCATION, String.format("%s/%s", request.url(), TEST_ID))
                                      .body(responseBody).build();

                if (request.method().equals(HttpMethod.POST)) {
                    keycloakPostWasExecuted.set(true);
                }

                return Mono.just(clientResponse);
            }).build();
        }

        return keycloakPostWasExecuted;
    }

    @Test
    public void given_a_clientid_when_creating_a_service_client_then_keycloak_post_clients_is_executed() throws JsonProcessingException {
        // Given
        var clients = new Map[]{Map.of("id", TEST_CLIENT_ID)};
        var responseBody = objectMapper.writeValueAsString(clients);

        var keycloakPostWasExecuted = mockWebClientResponses(responseBody);
        ReflectionTestUtils.setField(sut, "webClientWithAdminBearerAuth", webClient);
        ReflectionTestUtils.setField(sut, "keycloakBaseUrl", URI.create("http://localhost"));

        // When
        sut.createServiceClient(TEST_CLIENT_ID);

        //Then
        assertThat(keycloakPostWasExecuted).isTrue();
    }

    @Test
    public void given_a_clientid_when_getting_a_client_secret_then_a_client_secret_is_returned() throws JsonProcessingException {
        // Given
        var clientSecret = Map.of("value", TEST_CLIENT_SECRET);
        var responseBody = objectMapper.writeValueAsString(clientSecret);

        var keycloakPostWasExecuted = mockWebClientResponses(responseBody);
        ReflectionTestUtils.setField(sut, "webClientWithAdminBearerAuth", webClient);

        // When
        var actualClientSecret = sut.getClientSecret(TEST_CLIENT_ID);

        //Then
        assertThat(actualClientSecret).isEqualTo(TEST_CLIENT_SECRET);
    }

    @Test
    public void given_a_realm_name_when_creating_a_realm_then_keycloak_post_realms_is_executed() throws JsonProcessingException {
        // Given
        ReflectionTestUtils.setField(sut, "realmName", TEST_REALM);
        var realms = new Map[]{Map.of("realm", TEST_REALM)};
        var responseBody = objectMapper.writeValueAsString(realms);

        var keycloakPostWasExecuted = mockWebClientResponses(responseBody);
        ReflectionTestUtils.setField(sut, "webClientWithAdminBearerAuth", webClient);

        // When
        sut.createRealm();

        //Then
        assertThat(keycloakPostWasExecuted).isTrue();
    }

    @Test
    public void given_a_role_name_and_clientid_when_creating_a_role_for_client_then_keycloak_post_roles_is_executed() throws JsonProcessingException {
        // Given
        var roles = new Map[]{Map.of("name", TEST_ROLE)};
        var responseBody = objectMapper.writeValueAsString(roles);

        var keycloakPostWasExecuted = mockWebClientResponses(responseBody);
        ReflectionTestUtils.setField(sut, "webClientWithAdminBearerAuth", webClient);

        // When
        sut.createRoleForClient(TEST_ROLE, TEST_CLIENT_ID);

        //Then
        assertThat(keycloakPostWasExecuted).isTrue();
    }

    @Test
    public void given_client_id_and_secret_when_getJwtFromKeycloakForService_then_a_jwt_is_returned() throws JsonProcessingException {
        // Given
        var role = Map.of("access_token", TEST_JWT);
        var responseBody = objectMapper.writeValueAsString(role);

        mockWebClientResponses(responseBody);
        ReflectionTestUtils.setField(sut, "webClientWithoutAdminBearerAuth", webClient);

        // When
        var actualJwt = sut.getJwtFromKeycloakForService(TEST_CLIENT_ID, TEST_ROLE);

        //Then
        assertThat(actualJwt).isEqualTo(TEST_JWT);
    }

    @Test
    public void given_user_details_when_creating_a_user_then_keycloak_post_users_is_executed() throws JsonProcessingException {
        // Given
        var users = new Map[]{Map.of("username", TEST_USERNAME)};
        var responseBody = objectMapper.writeValueAsString(users);

        var keycloakPostWasExecuted = mockWebClientResponses(responseBody);
        ReflectionTestUtils.setField(sut, "webClientWithAdminBearerAuth", webClient);

        // When
        sut.createUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME);

        //Then
        assertThat(keycloakPostWasExecuted).isTrue();
    }

    @Test
    public void given_user_details_when_getJwtFromKeycloakForUser_then_a_jwt_is_returned() throws JsonProcessingException {
        // Given
        var role = Map.of("access_token", TEST_JWT);
        var responseBody = objectMapper.writeValueAsString(role);

        mockWebClientResponses(responseBody);
        ReflectionTestUtils.setField(sut, "webClientWithoutAdminBearerAuth", webClient);

        // When
        var actualJwt = sut.getJwtFromKeycloakForUser(TEST_USERNAME, TEST_PASSWORD, TEST_CLIENT_ID);

        //Then
        assertThat(actualJwt).isEqualTo(TEST_JWT);
    }

    @Test
    public void given_user_role_details_when_creating_a_role_mapping_then_keycloak_post_roles_is_executed() throws JsonProcessingException {
        // Given
        var role = Map.of("username", TEST_USERNAME);
        var responseBody = objectMapper.writeValueAsString(role);

        var keycloakPostWasExecuted = mockWebClientResponses(responseBody);
        ReflectionTestUtils.setField(sut, "webClientWithAdminBearerAuth", webClient);

        // When
        sut.createUserRoleMapping(TEST_ID, TEST_CLIENT_ID, TEST_ROLE);

        //Then
        assertThat(keycloakPostWasExecuted).isTrue();
    }

    @Test
    public void when_initializing_then_keycloak_post_token_is_executed() throws JsonProcessingException {
        // Given
        given(keycloakProperties.getAdmin()).willReturn(admin);

        var role = Map.of("access_token", TEST_JWT);
        var responseBody = objectMapper.writeValueAsString(role);

        var keycloakPostWasExecuted = mockWebClientResponses(responseBody);
        ReflectionTestUtils.setField(sut, "webClientWithoutAdminBearerAuth", webClient);

        // When
        sut.init();

        //Then
        assertThat(keycloakPostWasExecuted).isTrue();
    }
}