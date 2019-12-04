package uk.co.serin.thule.spring.boot.starter.security.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.security.context.DelegatingSecurityContextHolder;
import uk.co.serin.thule.security.oauth2.utils.Oauth2Utils;
import uk.co.serin.thule.spring.boot.starter.security.oauth2.testservice.Application;
import uk.co.serin.thule.spring.boot.starter.security.oauth2.testservice.TestFeignClient;

import java.util.Map;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("itest")
@AutoConfigureWireMock(port = 0)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FeignOauth2IntTest {
    public static final String CLIENT_ID = "gohenry-test-service";
    public static final String CLIENT_SECRET = "secret";
    public static final Set<GrantedAuthority> GRANTED_AUTHORITIES = Set.of(new SimpleGrantedAuthority("PUBLIC"));
    public static final int USER_ID = 1234567890;
    public static final String USER_NAME = "user";
    @Autowired
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;
    @Mock
    private OAuth2AuthenticationDetails oAuth2AuthenticationDetails;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestFeignClient testFeignClient;

    @After
    public void after() {
        delegatingSecurityContextHolder.clearContext();
    }

    @Test
    public void given_a_jwt_exists_in_client_service_when_invoking_a_remote_service_via_a_feign_client_then_the_jwt_should_be_propagated_to_that_remote_service() {
        // Given
        var clientServiceJwt = Oauth2Utils.createJwtOauth2AccessToken(USER_NAME, USER_ID, GRANTED_AUTHORITIES, CLIENT_ID, CLIENT_SECRET).getValue();
        createSecurityContext(clientServiceJwt);

        // When
        var remoteServiceAuthentication = testFeignClient.getAuthentication();

        // Then
        assertThat(remoteServiceAuthentication).containsKey("details");

        var remoteServiceJwt = ((Map) remoteServiceAuthentication.get("details")).get("tokenValue").toString();
        assertThat(remoteServiceJwt).isEqualTo(clientServiceJwt);
    }

    private void createSecurityContext(String clientServiceJwt) {
        // Create OAuth2Authentication
        var oAuth2Authentication = new OAuth2Authentication(new OAuth2Request() {
        }, null);
        oAuth2Authentication.setDetails(oAuth2AuthenticationDetails);

        // Ensure the Jwt will be returned from the OAuth2AuthenticationDetails
        given(oAuth2AuthenticationDetails.getTokenValue()).willReturn(clientServiceJwt);

        // Create Security Context on the thread
        delegatingSecurityContextHolder.setContext(new SecurityContextImpl(oAuth2Authentication));
    }

    @Test
    public void given_no_jwt_in_client_service_when_invoking_a_remote_service_via_a_feign_client_then_a_new_jwt_should_be_propagated_to_that_remote_service()
            throws JsonProcessingException {
        // Given
        var clientServiceJwt = Oauth2Utils.createJwtOauth2AccessToken(USER_NAME, USER_ID, GRANTED_AUTHORITIES, CLIENT_ID, CLIENT_SECRET).getValue();

        // Mock an Oauth2 Authorization call to create an oauth token
        var oauthTokenResponse = objectMapper.writeValueAsString(Map.of("access_token", clientServiceJwt));
        givenThat(post(urlEqualTo("/oauth/token")).willReturn(
                aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                           .withBody(oauthTokenResponse)));

        // When
        var remoteServiceAuthentication = testFeignClient.getAuthentication();

        // Then
        assertThat(remoteServiceAuthentication).containsKey("details");

        var remoteServiceJwt = ((Map) remoteServiceAuthentication.get("details")).get("tokenValue").toString();
        assertThat(remoteServiceJwt).isEqualTo(clientServiceJwt);
    }
}