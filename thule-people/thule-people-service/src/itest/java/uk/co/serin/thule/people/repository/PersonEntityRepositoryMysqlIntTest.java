package uk.co.serin.thule.people.repository;

import org.junit.AfterClass;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

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
@ContextConfiguration(initializers = PersonEntityRepositoryMysqlIntTest.MysqlInitializer.class)
public class PersonEntityRepositoryMysqlIntTest extends PersonEntityRepositoryBaseIntTest {
    private static final DockerCompose DOCKER_COMPOSE = new DockerCompose("src/test/docker/thule-people-tests/docker-compose-mysql.yml");

    @AfterClass
    public static void teardownClass() throws IOException {
        DOCKER_COMPOSE.down();
    }

    /**
     * Flyway is used to apply the sql migration scripts. However, Flyway will fail to connect to the Mysql database if the database has not been started.
     * Ideally the mysql docker container would be started via a @BeforeClass annotation, a constructor or static initializer. However, the test class
     * is not loaded until after the spring context has been initialized via the Junit SpringRunner, by which time the spring application context,
     * which includes the Flyway spring bean, has failed to initialize. Although the Spring application context has failed to initialize,
     * the SpringRunner will continue processing as normal, i.e. execute any @BeforeClass methods followed by @Before methods and then the actual tests.
     *
     * One option is to ignore this and when the SpringRunner tries to run a test (i.e. after executing the @BeforeClass method),
     * it will recognise that their is no Spring context and create one (in the same way that an application context is re-created when using the
     * @DirtiesContext annotation). This time it should succeed because the Flyway spring bean will be able to connect to the mysql database which has
     * been started via the @BeforeClass. The downside to this approach is that it can be extremely slow having to create another application context.
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