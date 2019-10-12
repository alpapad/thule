package uk.co.serin.thule.people;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.utils.docker.DockerCompose;

import java.io.IOException;

@ActiveProfiles("ctest")
@ContextConfiguration(initializers = ContractBaseTest.MysqlInitializer.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ContractBaseTest {
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/test/docker/thule-people-tests/docker-compose-mysql.yml");

    @BeforeClass
    public static void setUpClass() throws IOException {
        DOCKER_COMPOSE.up();
    }

    /**
     * Flyway is used to apply the sql migration scripts. However, Flyway will fail to connect to the Mysql database if the database has not been started.
     * Ideally the mysql docker container would be started via a @BeforeClass annotation, a constructor or static initializer. However, the outer test class
     * is not loaded until after the spring context has been initialized via the Junit SpringRunner, by which time the spring application context,
     * which includes the Flyway spring bean, has failed. Although the Spring application context has failed to initialize, the SpringRunner will continue
     * to try to run the tests.
     *
     * One option is to ignore this and when the the SpringRunner tries to run a test (i.e. after executing the @BeforeClass method),
     * it will recognise that their is no Spring context and create another one (in the same way that an application context is re-created when using the
     * @DirtiesContext annotation). This time it should succeed because the Flyway spring bean will be able to connect to the mysql database which has
     * been started via the @BeforeClass. The downside to this approach is that it can be very slow having to create another application context
     *
     * Another option is to provide Spring with a ApplicationContextInitializer to start the mysql docker container, which is executed after the context
     * has been created but not yet initialized, i.e. no Spring beans have yet been created. This way, when the SpringRunner tries to run the tests,
     * it already has an application context running. This is the approach taken here and is a performance improvement which can save considerable
     * developer time.
     */
    static class MysqlInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            try {
                DOCKER_COMPOSE.downAndUp();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
