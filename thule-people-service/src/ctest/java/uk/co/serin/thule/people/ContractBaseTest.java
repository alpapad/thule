package uk.co.serin.thule.people;

import org.mockserver.client.MockServerClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("ctest")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ContractBaseTest {
    static MockServerClient mockServerClient;
    @Container
    private static MockServerContainer mockServer = new MockServerContainer("5.10.0");
    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql").withUsername("root").withPassword(null);

    @DynamicPropertySource
    private static void addDynamicProperties(DynamicPropertyRegistry registry) {
        mockServerClient = new MockServerClient(mockServer.getContainerIpAddress(), mockServer.getServerPort());
        registry.add("mock.server.port", mockServer::getServerPort);
        registry.add("thule.peopleservice.mysql.port", mysql::getFirstMappedPort);
    }
}
