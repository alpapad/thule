package uk.co.serin.thule.email.docker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.time.Duration;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@Testcontainers
@SpringBootTest
public class EmailDockerTest {
    private static final String MAILHOG_ALIAS = "mailhog";
    @Container
    private static GenericContainer<?> mailhog = createMailhogContainer();
    @Container
    private static GenericContainer<?> springBootService = createSpringBootService();
    private String baseUrl;

    private static GenericContainer<?> createMailhogContainer() {
        return new GenericContainer<>("mailhog/mailhog").withNetwork(Network.SHARED).withNetworkAliases(MAILHOG_ALIAS);
    }

    private static GenericContainer<?> createSpringBootService() {
        return new GenericContainer("pooh:8084/thule-email-service")
                .dependsOn(mailhog)
                .waitingFor(Wait.forHttp("/actuator/health").forStatusCode(HttpStatus.OK.value()))
                .withEnv(Map.of("JDK_JAVA_OPTIONS", "-XX:InitialHeapSize=256m -XX:MaxHeapSize=256m -XX:MaxMetaspaceSize=256m",
                        "SPRING_CLOUD_CONFIG_ENABLED", FALSE.toString(),
                        "SPRING_CLOUD_KUBERNETES_ENABLED", FALSE.toString(),
                        "SPRING_MAIL_HOST", MAILHOG_ALIAS,
                        "SPRING_ZIPKIN_ENABLED", FALSE.toString(),
                        "THULE_SHARED_LOGGING_LOGSTASH_ENABLED", FALSE.toString(),
                        "THULE_SHARED_OAUTH2_RESOURCESERVER_JWS_ENABLED", FALSE.toString(),
                        "TZ", "Europe/London"))
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
        var actuatorUri = ActuatorUri.using(baseUrl + "/actuator/health");

        // When
        assertThat(actuatorUri)
                .waitingForMaximum(Duration.ofMinutes(5))

                //Then
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
                .jsonPath("$.name").isEqualTo("thule-email-service");
    }
}