package uk.co.serin.thule.people;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
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
public class PeopleDockerTest {
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Value("${thule.peopleservice.api.host}")
    private String peopleServiceApiHost;
    @Value("${thule.peopleservice.api.port}")
    private int peopleServiceApiPort;
    private String peopleServiceBaseUrl;

    @Before
    public void setUp() throws IOException {
        // Start docker containers
        new DockerCompose("src/dtest/docker/thule-people-docker-tests/docker-compose.yml").up();

        // Create base url
        peopleServiceBaseUrl = "http://" + peopleServiceApiHost + ":" + peopleServiceApiPort;

        // Setup OAuth2
        var jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(
                "user", "user", 0, Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId", "gmjtdvNVmQRz8bzw6ae");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        var actuatorUri = new ActuatorUri(URI.create(peopleServiceBaseUrl + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void when_finding_all_people_then_at_least_one_person_is_found() {
        // When
        var personResponseEntity = oAuth2RestTemplate
                .exchange(peopleServiceBaseUrl + "/people", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Map<String, Object>>() {
                });

        // Then
        var embedded = (Map) Objects.requireNonNull(personResponseEntity.getBody()).get("_embedded");
        var people = (List) embedded.get("people");

        assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(people).isNotEmpty();
    }
}