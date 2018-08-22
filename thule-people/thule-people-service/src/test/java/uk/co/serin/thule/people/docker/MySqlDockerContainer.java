package uk.co.serin.thule.people.docker;


import com.gohenry.utils.docker.DockerCompose;

import java.io.IOException;

public class MySqlDockerContainer {
    private static boolean mySqlContainerStarted;
    private static MySqlDockerContainer mySqlDockerContainer = new MySqlDockerContainer();
    private final DockerCompose dockerCompose = new DockerCompose("src/test/docker/thule-people-tests/docker-compose-mysql.yml");

    private MySqlDockerContainer() {
    }

    public static MySqlDockerContainer instance() {
        return mySqlDockerContainer;
    }

    public synchronized void startMySqlContainerIfDown() {
        try {
            if (!mySqlContainerStarted) {
                dockerCompose.downAndUp();
                mySqlContainerStarted = true;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized void stopMySqlContainerIfup() {
        try {
            dockerCompose.down();
            mySqlContainerStarted = false;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}