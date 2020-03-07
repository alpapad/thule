package uk.co.serin.thule.authentication.keycloak;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;

import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;
import java.time.Duration;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

public class KeycloakContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static final String JOHN_DOE_PASSWORD = "password";
    public static final String JOHN_DOE_USERNAME = "john.doe@serin-consultancy.co.uk";
    public static final String THULE_ROLE_NAME = "USER";
    public static final String THULE_TEST_SERVICE_CLIENT_ID = "thule-test-service";
    public static final String THULE_WEBAPP_CLIENT_ID = "thule-webapp";
    private static boolean initialized;
    private KeycloakProperties keycloakProperties;

    @Override
    public synchronized void initialize(ConfigurableApplicationContext applicationContext) {
        var yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("config/application-itest.yml"));
        yaml.afterPropertiesSet();
        var applicationProperties = yaml.getObject();

        keycloakProperties = new KeycloakProperties();
        keycloakProperties.setBaseUrl(applicationProperties.get("thule.keycloak.base-url").toString());
        keycloakProperties.setRealm(applicationProperties.get("thule.keycloak.realm").toString());

        if (!initialized) {
            startKeycloak();
            createKeycloakResources();
            initialized = true;
        }
    }

    private void startKeycloak() {
        var webClient = WebClient.builder().baseUrl(keycloakProperties.getBaseUrl() + "/auth").build();

        try {
            new DockerCompose("src/itest/docker/thule-authentication-service-tests/docker-compose.yml").downAndUp();
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
        var keycloakRepository = new KeycloakRepository(keycloakProperties);

        keycloakRepository.createRealm();

        keycloakRepository.createServiceClient(THULE_TEST_SERVICE_CLIENT_ID);
        keycloakRepository.createRoleForClient(THULE_ROLE_NAME, THULE_TEST_SERVICE_CLIENT_ID);

        keycloakRepository.createPublicClient(THULE_WEBAPP_CLIENT_ID);
        keycloakRepository.createRoleForClient(THULE_ROLE_NAME, THULE_WEBAPP_CLIENT_ID);

        var userId = keycloakRepository.createUser(JOHN_DOE_USERNAME, JOHN_DOE_PASSWORD, "John", "Doe");
        keycloakRepository.createUserRoleMapping(userId, THULE_TEST_SERVICE_CLIENT_ID, THULE_ROLE_NAME);
        keycloakRepository.createUserRoleMapping(userId, THULE_WEBAPP_CLIENT_ID, THULE_ROLE_NAME);
    }
}