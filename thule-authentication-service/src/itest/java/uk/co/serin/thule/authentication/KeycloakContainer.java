package uk.co.serin.thule.authentication;

import org.springframework.http.HttpStatus;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class KeycloakContainer extends GenericContainer<KeycloakContainer> {
    public static final int PORT = 8080;
    public static final String VERSION = "latest";
    private KeycloakResourceProvider keycloakResourceProvider;

    public KeycloakContainer() {
        this(VERSION);
    }

    public KeycloakContainer(String version) {
        super("jboss/keycloak:" + version);
        addExposedPorts(PORT);
        waitingFor(Wait.forHttp("/auth").forStatusCode(HttpStatus.OK.value()).withStartupTimeout(Duration.ofMinutes(5)));
    }

    @Override
    public void start() {
        super.start();
        keycloakResourceProvider.createResources(this);
    }

    public KeycloakContainer withKeycloakResourceProvider(KeycloakResourceProvider keycloakResourceProvider) {
        this.keycloakResourceProvider = keycloakResourceProvider;
        return self();
    }

    @FunctionalInterface
    public interface KeycloakResourceProvider {
        void createResources(KeycloakContainer keycloakContainer);
    }
}
