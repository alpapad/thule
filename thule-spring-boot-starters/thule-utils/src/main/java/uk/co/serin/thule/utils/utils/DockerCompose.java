package uk.co.serin.thule.utils.utils;

import java.io.IOException;

public class DockerCompose {
    private final String dockerComposeYmlFile;
    private Process dockerComposeUp;
    private ProcessBuilder processBuilder;

    public DockerCompose(String dockerComposeYmlFile) {
        this.dockerComposeYmlFile = dockerComposeYmlFile;
        processBuilder = new ProcessBuilder().inheritIO();
    }

    public void downAndUp() throws IOException {
        down();
        up();
    }

    public void down() throws IOException {
        Process dockerComposeDown = processBuilder.command("docker-compose", "-f", dockerComposeYmlFile, "down", "-v").start();
        try {
            dockerComposeDown.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (dockerComposeUp != null) {
            try {
                dockerComposeUp.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void up() throws IOException {
        dockerComposeUp = processBuilder.command("docker-compose", "-f", dockerComposeYmlFile, "up").start();
    }
}