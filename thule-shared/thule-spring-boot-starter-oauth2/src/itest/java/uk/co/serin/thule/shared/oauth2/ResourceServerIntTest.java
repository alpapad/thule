package uk.co.serin.thule.shared.oauth2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.serin.thule.spring.boot.starter.oauth2.autoconfiguration.Oauth2Properties;
import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.oauth2.Oauth2Utils;
import uk.co.serin.thule.utils.oauth2.PhpJwtAccessTokenConverter;
import uk.co.serin.thule.utils.oauth2.UserAuthenticationDetails;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceServerIntTest {
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private Oauth2Properties oauth2Properties;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        var jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(
                null, null, 1234567890, Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId", oauth2Properties.getSigningKey());
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));
    }

    @Test
    public void when_authenticated_then_access_should_be_granted() {
        // When
        var responseEntity =
                oAuth2RestTemplate.postForEntity(String.format("http://localhost:%s/hello", port), HttpEntity.EMPTY, String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void when_authenticated_using_a_php_generated_jwt_then_access_should_be_granted() {
        // Given
        var jwtOauth2AccessToken =
                createJwtOauth2AccessToken("userName", "password", Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId",
                        "gmjtdvNVmQRz8bzw6ae");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));

        var url = String.format("http://localhost:%s/hello", port);

        // When
        var responseEntity = oAuth2RestTemplate.postForEntity(url, null, null);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private OAuth2AccessToken createJwtOauth2AccessToken(String principal, String credentials, Collection<? extends GrantedAuthority> grantedAuthorities,
                                                         String clientId, String signingKey) {
        // Create OAuth2Authentication
        var oAuth2Request = new OAuth2Request(null, clientId, null,
                true, null, null, null, null, null);
        var userAuthentication = new UsernamePasswordAuthenticationToken(principal, credentials, grantedAuthorities);
        userAuthentication.setDetails(new UserAuthenticationDetails(999));
        var oAuth2Authentication = new OAuth2Authentication(oAuth2Request, userAuthentication);

        // Create OAuth2AccessToken
        var defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(new InMemoryTokenStore());
        var oAuth2AccessToken = defaultTokenServices.createAccessToken(oAuth2Authentication);

        //Create DefaultAccessTokenConverter
        var phpJwtAccessTokenConverter = new PhpJwtAccessTokenConverter();
        var javaPhpAuthenticationConverter = new SpringPhpUserAuthenticationConverter();
        phpJwtAccessTokenConverter.setUserTokenConverter(javaPhpAuthenticationConverter);

        // Create JwtAccessTokenConverter
        var jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setAccessTokenConverter(phpJwtAccessTokenConverter);
        jwtAccessTokenConverter.setSigningKey(signingKey);

        // Convert the oAuth2AccessToken to JWT format and return it
        return jwtAccessTokenConverter.enhance(oAuth2AccessToken, oAuth2Authentication);
    }

    @Test
    public void when_checking_health_then_health_status_is_up() {
        // Given
        var actuatorUri = ActuatorUri.of(String.format("http://localhost:%s/actuator/health", port));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void when_not_authenticated_then_access_should_be_denied() {
        // When
        var responseEntity = testRestTemplate.postForEntity("/hello", HttpEntity.EMPTY, String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @TestConfiguration
    static class ResourceServerIntTestConfiguration {
        @RestController
        public class HelloWorldController {
            @RequestMapping(value = "/hello")
            public String helloWorld() {
                return "Hello";
            }
        }
    }
}

