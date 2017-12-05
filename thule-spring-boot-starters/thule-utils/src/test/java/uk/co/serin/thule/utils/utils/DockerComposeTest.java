package uk.co.serin.thule.utils.utils;

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
    private DockerCompose dockerCompose = new DockerCompose(DOCKER_COMPOSE_FILE);
    @Mock
    private Process process;
    @Mock
    private ProcessBuilder processBuilder;

    @Test
    public void downAndUp_stops_and_starts_docker_compose_process() throws IOException {
        // Given
        ReflectionTestUtils.setField(dockerCompose, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "down", "-v")).willReturn(processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "up")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);

        // Then
        dockerCompose.downAndUp();

        // When
        verify(processBuilder, times(2)).start();
    }

    @Test
    public void down_stops_docker_compose_process() throws IOException, InterruptedException {
        // Given
        ReflectionTestUtils.setField(dockerCompose, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "down", "-v")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);
        given(process.waitFor()).willThrow(InterruptedException.class);

        // Then
        dockerCompose.down();

        // When
        verify(processBuilder).start();
    }

    @Test
    public void down_stops_docker_compose_process_ignoring_interrupt() throws IOException, InterruptedException {
        // Given
        ReflectionTestUtils.setField(dockerCompose, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "down", "-v")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);
        given(process.waitFor()).willThrow(InterruptedException.class);

        // Then
        dockerCompose.down();

        // When
        verify(processBuilder).start();
    }

    @Test
    public void upAndDown_stops_and_starts_docker_compose_process() throws IOException, InterruptedException {
        // Given
        ReflectionTestUtils.setField(dockerCompose, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "up")).willReturn(processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "down", "-v")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);

        // Then
        dockerCompose.up();
        dockerCompose.down();

        // When
        verify(processBuilder, times(2)).start();
    }

    @Test
    public void upAndDown_stops_and_starts_docker_compose_process_ignoring_interrupt() throws IOException, InterruptedException {
        // Given
        ReflectionTestUtils.setField(dockerCompose, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "up")).willReturn(processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "down", "-v")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);
        given(process.waitFor()).willThrow(InterruptedException.class).willThrow(InterruptedException.class);

        // Then
        dockerCompose.up();
        dockerCompose.down();

        // When
        verify(processBuilder, times(2)).start();
    }

    @Test
    public void up_starts_docker_compose_process() throws IOException {
        // Given
        ReflectionTestUtils.setField(dockerCompose, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, "up")).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);

        // Then
        dockerCompose.up();

        // When
        verify(processBuilder).start();
    }
}