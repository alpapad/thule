package uk.co.serin.thule.repository.mongodb;


import com.gohenry.utils.docker.DockerCompose;

import org.awaitility.Duration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.Socket;

import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

public class MongoDockerContainer {
    private static boolean mongoAvailable;
    private static boolean mongoContainerStarted;
    private static MongoDockerContainer mongoDockerContainer = new MongoDockerContainer();
    private final DockerCompose dockerCompose = new DockerCompose("src/itest/docker/thule-repository-mongodb-integration-tests/docker-compose-mongo.yml");

    private MongoDockerContainer() {
    }

    public static MongoDockerContainer instance() {
        return mongoDockerContainer;
    }

    public void startMongoContainerIfDown() {
        try {
            if (!mongoContainerStarted) {
                dockerCompose.downAndUp();
                mongoContainerStarted = true;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void stopMongoContainerIfUp() {
        try {
            dockerCompose.down();
            mongoContainerStarted = false;
            mongoAvailable = false;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void waitForMongoAvailabilty(Environment env) {
        // Ideally, this would be done as a static @BeforeClass method. However, we need access to
        // the spring environment which is autowired and hence cannot be static
        if (!mongoAvailable) {
            String mongodbHost = env.getProperty("thule.repositorymongodb.mongodb.host", "localhost");
            int mongodbPort = env.getProperty("thule.repositorymongodb.mongodb.port", Integer.class, 27017);

            // Wait until MongoDb is up by checking that the port is available
            given().ignoreExceptions().pollInterval(fibonacci()).
                    await().timeout(Duration.FIVE_MINUTES).
                    untilAsserted(() -> new Socket(mongodbHost, mongodbPort).close());
            mongoAvailable = true;
        }
    }
}