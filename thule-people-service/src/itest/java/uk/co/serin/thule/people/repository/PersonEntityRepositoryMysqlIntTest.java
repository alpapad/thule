package uk.co.serin.thule.people.repository;

import org.junit.AfterClass;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import uk.co.serin.thule.people.docker.MysqlContainerInitializer;
import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;

/**
 * MySql, Oracle, H2 and HSQL embedded database drivers are on the itest classpath. By default, the H2
 * database will be configured for use by Spring Boot. However, this test is to test MySql so
 * to disable the use of the test embedded database, use the following:
 *
 * <code>@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)</code>
 */
@ActiveProfiles("itest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = MysqlContainerInitializer.class)
public class PersonEntityRepositoryMysqlIntTest extends PersonEntityRepositoryBaseIntTest {
    private static final DockerCompose DOCKER_COMPOSE =  new DockerCompose("src/test/docker/thule-people-service-tests/docker-compose-mysql.yml");

    @AfterClass
    public static void teardownClass() throws IOException {
        DOCKER_COMPOSE.down();
    }
}