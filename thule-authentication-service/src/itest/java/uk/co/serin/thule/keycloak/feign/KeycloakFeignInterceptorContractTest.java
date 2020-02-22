package uk.co.serin.thule.keycloak.feign;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.SocketUtils;

import uk.co.serin.thule.keycloak.KeycloakContainerInitializer;
import uk.co.serin.thule.keycloak.KeycloakManager;
import uk.co.serin.thule.keycloak.feign.testservice.Application;
import uk.co.serin.thule.keycloak.feign.testservice.TestFeignClient;
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
@ContextConfiguration(initializers = {KeycloakFeignInterceptorContractTest.RandomPortInitializer.class, KeycloakContainerInitializer.class})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class KeycloakFeignInterceptorContractTest {
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    private KeycloakManager keycloakManager;
    @Autowired
    private TestFeignClient testFeignClient;

    @After
    public void after() {
        SecurityContextHolder.clearContext();
    }

    @Before
    public void before() {
        keycloakManager = new KeycloakManager(KeycloakContainerInitializer.KEYCLOAK_BASE_URL, KeycloakContainerInitializer.THULE_REALM_NAME);

        // Replace client secret of the thule-test-service with actual client secret created via the KeycloakContainerInitializer
        var thuleTestServiceClientSecret = keycloakManager.getClientSecret(KeycloakContainerInitializer.THULE_TEST_SERVICE_CLIENT_ID);
        var keycloakRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        ReflectionTestUtils.setField(keycloakRegistration, "clientSecret", thuleTestServiceClientSecret);
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
        var thuleTestServiceClientSecret = keycloakManager.getClientSecret(KeycloakContainerInitializer.THULE_TEST_SERVICE_CLIENT_ID);

        // Obtain JWT for thule-test-service from keycloak
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", KeycloakContainerInitializer.THULE_TEST_SERVICE_CLIENT_ID);
        body.add("client_secret", thuleTestServiceClientSecret);
        var jwtTokenValue = keycloakManager.getJwtFromKeycloak(KeycloakContainerInitializer.KEYCLOAK_TOKEN_ENDPOINT, body);

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
            var randomPort = SocketUtils.findAvailableTcpPort();
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "server.port=" + randomPort);
        }
    }
}