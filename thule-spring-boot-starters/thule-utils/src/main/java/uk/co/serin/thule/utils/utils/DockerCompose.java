package uk.co.serin.thule.utils.utils;

import java.io.IOException;
import java.util.stream.Stream;

public class DockerCompose {
    private final String[] dockerComposeCommandPrefix;
    private final ProcessBuilder processBuilder;
    private Process dockerComposeUp;

    public DockerCompose(String dockerComposeYmlFile) {
        dockerComposeCommandPrefix = new String[]{"docker-compose", "-f", dockerComposeYmlFile};
        processBuilder = new ProcessBuilder().inheritIO();
    }

    public Process command(String... arguments) throws IOException {
        String[] command = Stream.of(dockerComposeCommandPrefix, arguments).flatMap(Stream::of).toArray(String[]::new);
        return processBuilder.command(command).start();
    }

    public void downAndUp() throws IOException {
        down();
        up();
    }

    public void down() throws IOException {
        String[] command = Stream.of(dockerComposeCommandPrefix, new String[]{"down", "-v"}).flatMap(Stream::of).toArray(String[]::new);
        Process dockerComposeDown = processBuilder.command(command).start();
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
        String[] command = Stream.of(dockerComposeCommandPrefix, new String[]{"up"}).flatMap(Stream::of).toArray(String[]::new);
        dockerComposeUp = processBuilder.command(command).start();
    }
}