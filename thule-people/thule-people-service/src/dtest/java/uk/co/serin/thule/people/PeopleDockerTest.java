package uk.co.serin.thule.people;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.docker.DockerCompose;
import uk.co.serin.thule.utils.oauth2.Oauth2Utils;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser(username = "user", password = "user")
public class PeopleDockerTest {
    private static final String PEOPLE_PATH = "/people";
    private static DockerCompose dockerCompose = new DockerCompose("src/dtest/docker/thule-people-docker-tests/docker-compose.yml");
    @Autowired
    private Environment env;
    private OAuth2RestTemplate oAuth2RestTemplate;
    private String peopleServiceBaseUrl;

    @BeforeClass
    public static void setUpClass() throws IOException {
        dockerCompose.downAndUp();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        dockerCompose.down();
    }

    @Before
    public void setUp() {
        // Create base url
        String peopleServiceApiHost = env.getRequiredProperty("thule.peopleservice.api.host");
        int peopleServiceApiPort = env.getRequiredProperty("thule.peopleservice.api.port", Integer.class);
        peopleServiceBaseUrl = "http://" + peopleServiceApiHost + ":" + peopleServiceApiPort;

        // Setup OAuth2
        OAuth2AccessToken jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(
                "user", "user", 0, Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId", "gmjtdvNVmQRz8bzw6ae");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));
    }

    @Test
    public void when_finding_all_people_then_at_least_one_person_is_found() {
        // Given
        when_checking_health_then_status_is_up();

        // When
        ResponseEntity<Map<String, Object>> personResponseEntity
                = oAuth2RestTemplate.exchange(peopleServiceBaseUrl + PEOPLE_PATH, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Map<String, Object>>() {
        });

        // Then
        Map embedded = (Map) Objects.requireNonNull(personResponseEntity.getBody()).get("_embedded");
        List people = (List) embedded.get("people");

        assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(people).isNotEmpty();
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(peopleServiceBaseUrl + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }
}