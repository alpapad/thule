package uk.co.serin.thule.discovery;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.SocketUtils;

/**
 * Tests for the Discovery service.
 *
 * It is good practise for the tests to use a random unused port to ensure that their are no clashes with existing ports in use. If we use
 * SpringBootTest.WebEnvironment.RANDOM_PORT, this port will be used by the server as intended. However, the Eureka client that is required as part of
 * the server will be unaware of the random port because it is initialized before the random port is determined. To overcome this, we use a
 * ApplicationContextInitializer which sets the SpringBootTest.WebEnvironment.DEFINED_PORT, i.e. server.port, before the application is initialized.
 * This way both the application server and the eureka client can use a 'random' port.
 */
@ActiveProfiles("ctest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = ContractBaseTest.RandomPortInitializer.class)
abstract class ContractBaseTest {
    static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("server.port=" + SocketUtils.findAvailableTcpPort()).applyTo(applicationContext);
        }
    }
}
