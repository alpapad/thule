package uk.co.serin.thule.people;


import uk.co.serin.thule.utils.utils.DockerCompose;

import java.io.IOException;

public class MySqlDockerContainer {
    private static boolean mySqlContainerStarted;

    public static void startMySqlContainerIfDown() {
        try {
            if (!mySqlContainerStarted) {
                new DockerCompose("src/itest/docker/thule-people-service/docker-compose-mysql.yml").downAndUp();
                mySqlContainerStarted = true;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}