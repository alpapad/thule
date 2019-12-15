package uk.co.serin.thule.people;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.people.docker.MysqlContainerInitializer;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;

@ActiveProfiles("ctest")
@AutoConfigureWireMock(port = 0)
@ContextConfiguration(initializers = MysqlContainerInitializer.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ContractBaseTest {
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/test/docker/thule-people-service-tests/docker-compose-mysql.yml");

    @BeforeClass
    public static void setUpClass() throws IOException {
        DOCKER_COMPOSE.up();
    }
}
