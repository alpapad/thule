package uk.co.serin.thule.authentication.feign;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.SocketUtils;

import uk.co.serin.thule.authentication.KeycloakBaseIntTest;
import uk.co.serin.thule.authentication.feign.testservice.Application;
import uk.co.serin.thule.authentication.feign.testservice.TestFeignClient;
import uk.co.serin.thule.resourceserver.utils.JwtUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Feign.
 *
 * It is good practise for the tests to use a random unused port to ensure that their are no clashes with existing ports in use. If we use
 * SpringBootTest.WebEnvironment.RANDOM_PORT, this port will be used by the server as intended. However, the Feign Client uses a Load Balancer and
 * Discovery Service to determine where to route a request. The Load Balancer and Discovery Service are configured in the application yml file but
 * the Local Server Port created when using SpringBootTest.WebEnvironment.RANDOM_PORT is not available at runtime in application yml files.
 *
 * To overcome this, we use a ApplicationContextInitializer which sets the SpringBootTest.WebEnvironment.DEFINED_PORT, i.e. server.port,
 * before the application is initialized. This way both the application server and feign client can use a 'random' port.
 */
@ActiveProfiles({"itest", "itest-feign"})
@ContextConfiguration(initializers = KeycloakFeignInterceptorIntTest.RandomPortInitializer.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class KeycloakFeignInterceptorIntTest extends KeycloakBaseIntTest {
    @Autowired
    private TestFeignClient testFeignClient;

    @DynamicPropertySource
    public static void addClientSecret(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.security.oauth2.client.registration.keycloak.client-secret",
                () -> getKeycloakRepository().getClientSecret(KeycloakBaseIntTest.THULE_TEST_SERVICE_CLIENT_ID));
    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void given_a_jwt_in_security_context_when_invoking_a_remote_service_via_a_feign_client_then_the_service_should_execute_successfully() {
        // Given
        insertJwtIntoTheSecurityContext();

        // When
        var feignClientResponse = testFeignClient.hello();

        // Then
        assertThat(feignClientResponse).isEqualTo("Hello World");
    }

    private void insertJwtIntoTheSecurityContext() {
        // Obtain thule-test-service client secret from keycloak
        var thuleTestServiceClientSecret = getKeycloakRepository().getClientSecret(KeycloakBaseIntTest.THULE_TEST_SERVICE_CLIENT_ID);

        // Obtain JWT for thule-test-service from keycloak
        var jwtTokenValue =
                getKeycloakRepository().getJwtFromKeycloakForService(KeycloakBaseIntTest.THULE_TEST_SERVICE_CLIENT_ID, thuleTestServiceClientSecret);

        // Convert JWT from keycloak into a spring security Jwt object
        var jwt = JwtUtils.createKeycloakJwt(jwtTokenValue);

        // Create OAuth2Authentication
        var jwtAuthenticationToken = new JwtAuthenticationToken(jwt);

        // Create Security Context on the thread
        SecurityContextHolder.setContext(new SecurityContextImpl(jwtAuthenticationToken));
    }

    @Test
    public void given_no_jwt_in_security_context_when_invoking_a_remote_service_via_a_feign_client_then_the_service_should_execute_successfully() {
        // Given
        SecurityContextHolder.clearContext();

        // When
        var feignClientresponse = testFeignClient.hello();

        // Then
        assertThat(feignClientresponse).isEqualTo("Hello World");
    }

    static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("server.port=" + SocketUtils.findAvailableTcpPort()).applyTo(applicationContext);
        }
    }
}