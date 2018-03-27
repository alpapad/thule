package uk.co.serin.thule.repository.mongodb;


import org.awaitility.Duration;
import org.springframework.core.env.Environment;

import uk.co.serin.thule.utils.utils.DockerCompose;

import java.io.IOException;
import java.net.Socket;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

public class MongoDockerContainer {
    private static boolean mongoAvailable;
    private static boolean mongoContainerStarted;
    private Environment env;

    public MongoDockerContainer(Environment env) {
        this.env = env;
    }

    public static void startRabbitContainerIfDown() {
        try {
            if (!mongoContainerStarted) {
                new DockerCompose("src/itest/docker/thule-repository-mongodb/docker-compose-mongo.yml").downAndUp();
                mongoContainerStarted = true;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void stopMongoContainerIfUp() {
        try {
            if (mongoContainerStarted) {
                mongoAvailable = false;
                new DockerCompose("src/itest/docker/thule-repository-mongodb/docker-compose-mongo.yml").down();
                mongoContainerStarted = false;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void waitForMongoAvailabilty() {
        // Ideally, this would be done as a static @BeforeClass method. However, we need access to
        // the spring environment which is autowired and hence cannot be static
        if (!mongoAvailable) {
            String mongodbHost = env.getRequiredProperty("thule.thule-repository-mongodb.mongodb-host");
            int mongodbPort = env.getRequiredProperty("thule.thule-repository-mongodb.mongodb-port", Integer.class);

            // Wait until MongoDb is up by checking that the port is available
            given().ignoreExceptions().pollInterval(fibonacci()).
                    await().timeout(Duration.TWO_MINUTES).
                    untilAsserted(() -> new Socket(mongodbHost, mongodbPort).close());
            mongoAvailable = true;
        }
    }
}