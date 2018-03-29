package uk.co.serin.thule.people;

import uk.co.serin.thule.utils.utils.DockerCompose;

import java.io.IOException;

public class MySqlDockerContainer {
    private static MySqlDockerContainer mySqlDockerContainer = new MySqlDockerContainer();
    private final DockerCompose dockerCompose = new DockerCompose("src/itest/docker/thule-people-service/docker-compose-mysql.yml");
    private boolean mySqlContainerStarted;

    private MySqlDockerContainer() {
    }

    public static MySqlDockerContainer instance() {
        return mySqlDockerContainer;
    }

    public void startMySqlContainerIfDown() {
        try {
            if (!mySqlContainerStarted) {
                dockerCompose.downAndUp();
                mySqlContainerStarted = true;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void stopMySqlContainerIfup() {
        try {
            dockerCompose.down();
            mySqlContainerStarted = false;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}