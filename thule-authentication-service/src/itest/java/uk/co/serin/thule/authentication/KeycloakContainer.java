package uk.co.serin.thule.authentication;

import org.springframework.http.HttpStatus;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import uk.co.serin.thule.authentication.keycloak.KeycloakProperties;
import uk.co.serin.thule.authentication.keycloak.KeycloakRepository;

import java.net.URI;
import java.time.Duration;

public class KeycloakContainer extends GenericContainer<KeycloakContainer> {
    public static final String JOHN_DOE_PASSWORD = "password";
    public static final String JOHN_DOE_USERNAME = "john.doe@serin-consultancy.co.uk";
    public static final int PORT = 8080;
    public static final String THULE_TEST_SERVICE_CLIENT_ID = "thule-test-service";
    public static final String THULE_WEBAPP_CLIENT_ID = "thule-webapp";
    public static final String VERSION = "latest";
    private static final String THULE_ROLE_NAME = "USER";
    private KeycloakProperties keycloakProperties = new KeycloakProperties();
    private KeycloakRepository keycloakRepository;

    public KeycloakContainer() {
        this(VERSION);
    }

    public KeycloakContainer(String version) {
        super("jboss/keycloak:" + version);
        addExposedPorts(PORT);
        waitingFor(Wait.forHttp("/auth").forStatusCode(HttpStatus.OK.value()).withStartupTimeout(Duration.ofMinutes(5)));
    }

    public KeycloakRepository getKeycloakRepository() {
        return keycloakRepository;
    }

    @Override
    public void start() {
        super.start();
        createKeycloakResources();
    }

    private void createKeycloakResources() {
        keycloakProperties.setBaseUrl(URI.create(String.format("http://%s:%s", getContainerIpAddress(), getMappedPort(PORT))));
        keycloakRepository = new KeycloakRepository(keycloakProperties);
        keycloakRepository.init();

        keycloakRepository.createRealm();

        keycloakRepository.createServiceClient(THULE_TEST_SERVICE_CLIENT_ID);
        keycloakRepository.createRoleForClient(THULE_ROLE_NAME, THULE_TEST_SERVICE_CLIENT_ID);

        keycloakRepository.createPublicClient(THULE_WEBAPP_CLIENT_ID);
        keycloakRepository.createRoleForClient(THULE_ROLE_NAME, THULE_WEBAPP_CLIENT_ID);

        var userId = keycloakRepository.createUser(JOHN_DOE_USERNAME, JOHN_DOE_PASSWORD, "John", "Doe");
        keycloakRepository.createUserRoleMapping(userId, THULE_TEST_SERVICE_CLIENT_ID, THULE_ROLE_NAME);
        keycloakRepository.createUserRoleMapping(userId, THULE_WEBAPP_CLIENT_ID, THULE_ROLE_NAME);
    }

    public KeycloakContainer withKeycloakProperties(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
        return self();
    }
}
