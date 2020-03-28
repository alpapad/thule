package uk.co.serin.thule.people;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("ctest")
@AutoConfigureWireMock(port = 0)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class ContractBaseTest {
    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql").withUsername("root").withPassword(null);

    @DynamicPropertySource
    private static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("thule.peopleservice.mysql.port", mysql::getFirstMappedPort);
    }
}
