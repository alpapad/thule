package uk.co.serin.thule.authentication;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import uk.co.serin.thule.authentication.keycloak.KeycloakProperties;
import uk.co.serin.thule.authentication.keycloak.KeycloakRepository;

import java.util.Map;

@Testcontainers
public class KeycloakBaseIntTest {
    private static final String KEYCLOAK_PASSWORD = "admin";
    private static final String KEYCLOAK_USER = "admin";
    private static final String MYSQL_ALIAS = "mysql";
    private static final String MYSQL_PASSWORD = "keycloak";
    private static final String MYSQL_USER = "keycloak";
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
                        "KEYCLOAK_PASSWORD", KEYCLOAK_PASSWORD,
                        "TZ", "Europe/London"
                ))
                .withKeycloakProperties(retrieveKeycloakProperties())
                .withNetwork(Network.SHARED);
    }

    private static KeycloakProperties retrieveKeycloakProperties() {
        var factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(new ClassPathResource("config/application-itest.yml"));

        var properties = factoryBean.getObject();

        var propertySource = new MapConfigurationPropertySource(properties);
        var binder = new Binder(propertySource);

        return binder.bind("thule.keycloak", KeycloakProperties.class).get();
    }

    private static MySQLContainer<?> createMySqlContainer() {
        return new MySQLContainer<>("mysql:5.7")
                .withDatabaseName("keycloak")
                .withNetwork(Network.SHARED)
                .withNetworkAliases(MYSQL_ALIAS)
                .withUsername(MYSQL_USER)
                .withPassword(MYSQL_PASSWORD);
    }

    public static KeycloakRepository getKeycloakRepository() {
        return keycloak.getKeycloakRepository();
    }
}