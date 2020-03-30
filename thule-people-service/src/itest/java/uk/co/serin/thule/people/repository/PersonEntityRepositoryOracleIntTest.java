package uk.co.serin.thule.people.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;

/**
 * MySql, Oracle, H2 and HSQL embedded database drivers are on the itest classpath. By default, the H2
 * database will be configured for use by Spring Boot. However, this test is to test Oracle so
 * to disable the use of the test embedded database, use the following:
 *
 * <code>@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)</code>
 */
@ActiveProfiles({"itest", "itest-oracle"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonEntityRepositoryOracleIntTest extends PersonEntityRepositoryBaseIntTest {
    @Container
    private static OracleContainer oracle =
            new OracleContainer("pooh:8082/oracle-database:18.4.0-xe-initialized").withEnv("ORACLE_PDB", "system").withEnv("ORACLE_PWD", "oracle");

    @DynamicPropertySource
    private static void oracleProperties(DynamicPropertyRegistry registry) {
        registry.add("thule.peopleservice.oracle.port", oracle::getFirstMappedPort);
    }
}