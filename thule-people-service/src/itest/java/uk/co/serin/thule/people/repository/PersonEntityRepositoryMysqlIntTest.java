package uk.co.serin.thule.people.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

/**
 * MySql, Oracle, H2 and HSQL embedded database drivers are on the itest classpath. By default, the H2
 * database will be configured for use by Spring Boot. However, this test is to test MySql so
 * to disable the use of the test embedded database, use the following:
 *
 * <code>@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)</code>
 */
@ActiveProfiles("itest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonEntityRepositoryMysqlIntTest extends PersonEntityRepositoryBaseIntTest {
    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql").withUsername("root").withPassword(null);

    @DynamicPropertySource
    private static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("thule.peopleservice.mysql.port", mysql::getFirstMappedPort);
    }
}