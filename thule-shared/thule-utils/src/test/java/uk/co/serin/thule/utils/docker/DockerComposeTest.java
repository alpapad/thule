package uk.co.serin.thule.utils.docker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DockerComposeTest {
    private static final String DOCKER_COMPOSE_FILE = "docker-compose.yml";
    @Mock
    private Process process;
    @Mock
    private ProcessBuilder processBuilder;
    private DockerCompose sut = new DockerCompose(DOCKER_COMPOSE_FILE);

    @Test
    public void given_an_interrupt_when_up_and_down_then_docker_compose_starts_and_stops_ignoring_the_interrupt() throws IOException, InterruptedException {
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
    public void when_constructing_with_environment_variables_then_they_are_set_in_the_docker_compose_process() {
        // Given
        var environmentVariables = Collections.singletonMap("name", "value");

        // Then
        sut = new DockerCompose(DOCKER_COMPOSE_FILE, environmentVariables);

        // When
        var processBuilder = (ProcessBuilder) ReflectionTestUtils.getField(sut, "processBuilder");
        assertThat(processBuilder.environment()).containsAllEntriesOf(environmentVariables);
    }

    @Test
    public void when_down_and_up_then_docker_compose_stops_and_starts() throws IOException {
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
    public void when_down_then_docker_compose_stops() throws IOException, InterruptedException {
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
    public void when_executing_a_command_then_docker_compose_command_starts() throws IOException {
        // Given
        var dockerComposeCommand = "ps";
        ReflectionTestUtils.setField(sut, "processBuilder", processBuilder);
        given(processBuilder.command("docker-compose", "-f", DOCKER_COMPOSE_FILE, dockerComposeCommand)).willReturn(processBuilder);
        given(processBuilder.start()).willReturn(process);

        // Then
        sut.command(dockerComposeCommand);

        // When
        verify(processBuilder).start();
    }

    @Test
    public void when_up_and_down_then_docker_compose_starts_and_stops() throws IOException {
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
    public void when_up_then_docker_compose_starts() throws IOException {
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