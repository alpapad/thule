package uk.co.serin.thule.people;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;

@ActiveProfiles("ctest")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ContractBaseTest {
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/test/docker/thule-people-tests/docker-compose-mysql.yml");

    @BeforeClass
    public static void setUpClass() throws IOException {
        DOCKER_COMPOSE.up();
    }
}
