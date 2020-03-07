package uk.co.serin.thule.utils.docker;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

public class DockerCompose {
    private final String[] dockerComposeCommandPrefix;
    private final ProcessBuilder processBuilder;
    private Process dockerComposeUp;

    public DockerCompose(String dockerComposeYmlFile, Map<String, String> environment) {
        this(dockerComposeYmlFile);
        processBuilder.environment().putAll(environment);
    }

    public DockerCompose(String dockerComposeYmlFile) {
        dockerComposeCommandPrefix = new String[]{"docker-compose", "-f", dockerComposeYmlFile};
        processBuilder = new ProcessBuilder().inheritIO();
    }

    public Process command(String... arguments) throws IOException {
        var command = Stream.of(dockerComposeCommandPrefix, arguments).flatMap(Stream::of).toArray(String[]::new);
        return processBuilder.command(command).start();
    }

    public void downAndUp() throws IOException {
        down();
        up();
    }

    public void down() throws IOException {
        var arguments = new String[]{"down", "-v", "--remove-orphans"};
        var command = Stream.of(dockerComposeCommandPrefix, arguments).flatMap(Stream::of).toArray(String[]::new);
        var dockerComposeDown = processBuilder.command(command).start();
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
        var arguments = new String[]{"up"};
        var command = Stream.of(dockerComposeCommandPrefix, arguments).flatMap(Stream::of).toArray(String[]::new);
        dockerComposeUp = processBuilder.command(command).start();
    }
}