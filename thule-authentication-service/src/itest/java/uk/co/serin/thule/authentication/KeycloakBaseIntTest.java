package uk.co.serin.thule.authentication;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import uk.co.serin.thule.authentication.keycloak.KeycloakProperties;
import uk.co.serin.thule.authentication.keycloak.KeycloakRepository;

import java.net.URI;
import java.util.Map;

@Testcontainers
public class KeycloakBaseIntTest {
    protected static final String JOHN_DOE_PASSWORD = "password";
    protected static final String JOHN_DOE_USERNAME = "john.doe@serin-consultancy.co.uk";
    protected static final String THULE_TEST_SERVICE_CLIENT_ID = "thule-test-service";
    protected static final String THULE_WEBAPP_CLIENT_ID = "thule-webapp";
    private static final String KEYCLOAK_PASSWORD = "admin";
    private static final String KEYCLOAK_USER = "admin";
    private static final String MYSQL_ALIAS = "mysql";
    private static final String MYSQL_DATABASE_NAME = "keycloak";
    private static final String MYSQL_PASSWORD = "keycloak";
    private static final String MYSQL_USER = "keycloak";
    private static final String THULE_ROLE_NAME = "USER";
    private static KeycloakRepository keycloakRepository;

    @Container
    private static MySQLContainer<?> mysql = createMySqlContainer();
    @Container
    private static KeycloakContainer keycloak = createKeycloakContainer();

    @DynamicPropertySource
    public static void addBaseUrl(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("thule.keycloak.base-url",
                () -> String.format("http://%s:%s", keycloak.getContainerIpAddress(), keycloak.getMappedPort(KeycloakContainer.PORT)));
    }

    private static KeycloakContainer createKeycloakContainer() {
        return new KeycloakContainer()
                .dependsOn(mysql)
                .withEnv(Map.of("DB_VENDOR", "mysql",
                        "DB_ADDR", MYSQL_ALIAS,
                        "DB_PASSWORD", MYSQL_PASSWORD,
                        "DB_USER", MYSQL_USER,
                        "JDBC_PARAMS", "useSSL=false",
                        "KEYCLOAK_USER", KEYCLOAK_USER,
                        "KEYCLOAK_PASSWORD", KEYCLOAK_PASSWORD
                ))
                .withImagePullPolicy(PullPolicy.alwaysPull())
                .withKeycloakResourceProvider(createKeycloakResourceProvider())
                .withNetwork(Network.SHARED);
    }

    private static KeycloakContainer.KeycloakResourceProvider createKeycloakResourceProvider() {
        return keycloakContainer -> {
            var keycloakProperties = retrieveKeycloakProperties();
            keycloakProperties.setBaseUrl(URI.create(
                    String.format("http://%s:%s", keycloakContainer.getContainerIpAddress(), keycloakContainer.getMappedPort(KeycloakContainer.PORT))));

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
        };
    }

    private static KeycloakProperties retrieveKeycloakProperties() {
        var factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(new ClassPathResource("config/application-itest.yml"));

        var properties = factoryBean.getObject();

        var propertySource = new MapConfigurationPropertySource(properties);
        var binder = new Binder(propertySource);

        return binder.bind("thule.keycloak", KeycloakProperties.class).get(); // Must be the same prefix as @ConfigurationProperties
    }

    private static MySQLContainer<?> createMySqlContainer() {
        return new MySQLContainer<>("mysql:5.7")
                .withDatabaseName(MYSQL_DATABASE_NAME)
                .withImagePullPolicy(PullPolicy.alwaysPull())
                .withNetwork(Network.SHARED)
                .withNetworkAliases(MYSQL_ALIAS)
                .withUsername(MYSQL_USER)
                .withPassword(MYSQL_PASSWORD);
    }

    protected static KeycloakRepository getKeycloakRepository() {
        return keycloakRepository;
    }
}