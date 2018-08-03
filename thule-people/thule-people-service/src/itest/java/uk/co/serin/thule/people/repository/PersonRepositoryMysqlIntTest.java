package uk.co.serin.thule.people.repository;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import uk.co.serin.thule.people.docker.MySqlDockerContainer;

/**
 * MySql, Oracle, H2 and HSQL embedded database drivers are on the itest classpath. By default, the H2
 * database will be configured for use by Spring Boot. However, this test is to test MySql so
 * to disable the use of the test embedded database, use the following:
 *
 * <code>@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)</code>
 */
@ActiveProfiles("itest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryMysqlIntTest extends PersonRepositoryBaseIntTest {
    @BeforeClass
    public static void setUpClass() {
        MySqlDockerContainer.instance().startMySqlContainerIfDown();
    }
    @AfterClass
    public static void tearDownClass() {
        MySqlDockerContainer.instance().stopMySqlContainerIfup();
    }
}