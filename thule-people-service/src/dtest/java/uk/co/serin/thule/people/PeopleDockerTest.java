package uk.co.serin.thule.people;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.time.Duration;
import java.util.HashMap;

import static java.lang.Boolean.FALSE;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@Testcontainers
@SpringBootTest
public class PeopleDockerTest {
    private static final String MYSQL_ALIAS = "mysql";
    @Container
    private static MySQLContainer<?> mysql = createMySqlContainer();
    @Container
    private static GenericContainer<?> springBootService = createSpringBootService();
    private String baseUrl;

    private static MySQLContainer<?> createMySqlContainer() {
        return new MySQLContainer<>("mysql")
                .withImagePullPolicy(PullPolicy.alwaysPull())
                .withNetwork(Network.SHARED)
                .withNetworkAliases(MYSQL_ALIAS)
                .withUsername("root")
                .withPassword(null);
    }

    private static GenericContainer<?> createSpringBootService() {
        var environmentVariables = new HashMap<>();
        environmentVariables.put("JDK_JAVA_OPTIONS", "-XX:InitialHeapSize=256m -XX:MaxHeapSize=256m -XX:MaxMetaspaceSize=256m");
        environmentVariables.put("SPRING_CLOUD_CONFIG_ENABLED", FALSE.toString());
        environmentVariables.put("SPRING_CLOUD_KUBERNETES_ENABLED", FALSE.toString());
        environmentVariables.put("SPRING_DATASOURCE_USERNAME", "root");
        environmentVariables.put("SPRING_FLYWAY_ENABLED", FALSE.toString());
        environmentVariables.put("SPRING_ZIPKIN_ENABLED", FALSE.toString());
        environmentVariables.put("THULE_PEOPLESERVICE_MYSQL_HOST", MYSQL_ALIAS);
        environmentVariables.put("THULE_PEOPLESERVICE_MYSQL_SCHEMA", "mysql");
        environmentVariables.put("THULE_SHARED_LOGGING_LOGSTASH_ENABLED", FALSE.toString());
        environmentVariables.put("THULE_SHARED_OAUTH2_RESOURCESERVER_JWS_ENABLED", FALSE.toString());
        environmentVariables.put("TZ", "Europe/London");

        return new GenericContainer("pooh:8084/thule-people-service")
                .dependsOn(mysql)
                .waitingFor(Wait.forHttp("/actuator/health").forStatusCode(HttpStatus.OK.value()))
                .withEnv(environmentVariables)
                .withExposedPorts(8080)
                .withNetwork(Network.SHARED);
    }

    @BeforeEach
    public void beforeEach() {
        baseUrl = String.format("http://%s:%s", springBootService.getContainerIpAddress(), springBootService.getFirstMappedPort());
    }

    @Test
    public void given_docker_container_has_been_started_when_checking_health_then_status_is_up() {
        // Given
        var actuatorUri = ActuatorUri.using(String.format("%s/actuator/health", baseUrl));

        // When
        assertThat(actuatorUri)
                .waitingForMaximum(Duration.ofMinutes(5))

                // Then
                .hasHealthStatus(Status.UP);
    }

    @Test
    public void given_docker_container_has_been_started_when_retrieving_actuator_info_then_it_shows_correct_microservice_is_in_docker_container() {
        // When
        WebTestClient
                .bindToServer().baseUrl(baseUrl).build()
                .get().uri("/actuator/info")
                .exchange()
                .expectStatus().isOk()
                .expectBody()

                // Then
                .jsonPath("$.name").isNotEmpty()
                .jsonPath("$.name").isEqualTo("thule-people-service");
    }
}