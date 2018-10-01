package uk.co.serin.thule.utils.docker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DockerComposeTest {
    private static final String DOCKER_COMPOSE_FILE = "docker-compose.yml";
    private DockerCompose sut = new DockerCompose(DOCKER_COMPOSE_FILE);
    @Mock
    private Process process;
    @Mock
    private ProcessBuilder processBuilder;

    @Test
    public void docker_compose_command_starts() throws IOException {
        // Given
        String dockerComposeCommand = "ps";
        ReflectionTestUtils.setField(sut, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, dockerComposeCommand)).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);

        // Then
        sut.command(dockerComposeCommand);

        // When
        verify(processBuilder).start();
    }

    @Test
    public void downAndUp_stops_and_starts_docker_compose_process() throws IOException {
        // Given
        ReflectionTestUtils.setField(sut, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "down", "-v", "--remove-orphans")).willReturn(processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "up")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);

        // Then
        sut.downAndUp();

        // When
        verify(processBuilder, times(2)).start();
    }

    @Test
    public void down_stops_docker_compose_process() throws IOException, InterruptedException {
        // Given
        ReflectionTestUtils.setField(sut, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "down", "-v", "--remove-orphans")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);
        given(process.waitFor()).willThrow(InterruptedException.class);

        // Then
        sut.down();

        // When
        verify(processBuilder).start();
    }

    @Test
    public void upAndDown_stops_and_starts_docker_compose_process() throws IOException, InterruptedException {
        // Given
        ReflectionTestUtils.setField(sut, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "up")).willReturn(processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "down", "-v", "--remove-orphans")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);

        // Then
        sut.up();
        sut.down();

        // When
        verify(processBuilder, times(2)).start();
    }

    @Test
    public void upAndDown_stops_and_starts_docker_compose_process_ignoring_interrupt() throws IOException, InterruptedException {
        // Given
        ReflectionTestUtils.setField(sut, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "up")).willReturn(processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "down", "-v", "--remove-orphans")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);
        given(process.waitFor()).willThrow(InterruptedException.class).willThrow(InterruptedException.class);

        // Then
        sut.up();
        sut.down();

        // When
        verify(processBuilder, times(2)).start();
    }

    @Test
    public void up_starts_docker_compose_process() throws IOException {
        // Given
        ReflectionTestUtils.setField(sut, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "up")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);

        // Then
        sut.up();

        // When
        verify(processBuilder).start();
    }
}