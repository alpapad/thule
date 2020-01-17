package uk.co.serin.thule.spring.boot.starter.security.oauth2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import uk.co.serin.thule.security.oauth2.utils.Oauth2Utils;
import uk.co.serin.thule.spring.boot.starter.security.oauth2.testservice.Application;

import java.util.Set;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ActiveProfiles("itest")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceServerAutoConfigurationIntTest {
    public static final Set<GrantedAuthority> GRANTED_AUTHORITIES = Set.of(new SimpleGrantedAuthority("PUBLIC"));
    public static final int USER_ID = 1234567890;
    public static final String USER_NAME = "user";
    private OAuth2RestTemplate oAuth2RestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void given_an_access_control_request_method_http_header_when_making_an_options_request_then_access_should_be_granted() {
        // Given
        var httpHeaders = new LinkedMultiValueMap<String, String>();
        httpHeaders.add(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name());
        httpHeaders.add(HttpHeaders.ORIGIN, "http://localhost");

        // When
        var responseEntity = testRestTemplate.exchange("/hello", HttpMethod.OPTIONS, new HttpEntity<>(httpHeaders), String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void given_no_access_control_request_method_http_header_when_making_an_options_request_then_access_should_be_denied() {
        // When
        var responseEntity = testRestTemplate.exchange("/hello", HttpMethod.OPTIONS, HttpEntity.EMPTY, String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void when_authenticated_using_a_java_generated_jwt_then_access_should_be_granted() {
        // Given
        var jwtOauth2AccessToken =
                Oauth2Utils.createJwtOauth2AccessToken(USER_NAME, USER_ID, GRANTED_AUTHORITIES, "gohenry-test-service", "secret");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));

        // When
        var responseEntity = oAuth2RestTemplate.getForEntity("http://localhost:{port}/hello", String.class, port);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void when_authenticated_using_a_php_generated_jwt_then_access_should_be_granted() {
        // Given
        var jwtOauth2AccessToken =
                Oauth2Utils.createPhpJwtOauth2AccessToken(USER_NAME, USER_ID, GRANTED_AUTHORITIES, "gohenry-test-service", "secret");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));

        // When
        var responseEntity = oAuth2RestTemplate.getForEntity("http://localhost:{port}/hello", String.class, port);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void when_not_authenticated_then_access_should_be_denied() {
        // When
        var responseEntity = testRestTemplate.getForEntity("/hello", String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}