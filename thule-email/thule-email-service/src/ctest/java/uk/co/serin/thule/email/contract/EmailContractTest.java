package uk.co.serin.thule.email.contract;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import uk.co.serin.thule.email.TestDataFactory;
import uk.co.serin.thule.email.domain.Email;
import uk.co.serin.thule.utils.oauth2.Oauth2Utils;

import java.util.Collections;

import static org.awaitility.Awaitility.await;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

public class EmailContractTest extends ContractBaseTest {
    private String emailServiceUrl;
    private OAuth2RestTemplate oAuth2RestTemplate;
    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        emailServiceUrl = String.format("http://localhost:%s/%s", port, Email.ENTITY_NAME_EMAILS);

        var jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(
                "username", "password", 0, Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId", "gmjtdvNVmQRz8bzw6ae");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));
    }

    @Test
    public void when_sending_an_email_then_email_is_sent_by_the_smtp_server() {
        //Given
        startEmbeddedSmtpServer();

        ParameterizedTypeReference<Email> responseType = new ParameterizedTypeReference<>() {
        };
        var expectedEmail = TestDataFactory.buildEmail();
        var expectedAttachment = expectedEmail.getAttachments().stream().findFirst().orElseThrow();
        var httpEntity = new HttpEntity<>(expectedEmail);

        // When
        var emailServiceResponse = oAuth2RestTemplate.exchange(emailServiceUrl, HttpMethod.POST, httpEntity, responseType);

        //Then
        await().until(() -> getSmtpServer().getEmailCount() > 0);
        assertThat(getSmtpServer().getEmailCount()).isEqualTo(1);

        assertThat(emailServiceResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        var actualMailMessage = getSmtpServer().getMessage(0);
        assertThat(actualMailMessage.getBody()).contains(expectedEmail.getBody());
        assertThat(actualMailMessage.getBody()).contains(expectedAttachment.getContent());
        assertThat(actualMailMessage.getFirstHeaderValue("From")).isEqualTo(expectedEmail.getFrom());
        var expectedTos = expectedEmail.getTos().toArray(new String[0]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[0]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[1]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[2]);
    }

    @Test
    public void when_smtp_server_is_down_then_response_should_be_accepted() {
        //Given
        ParameterizedTypeReference<Email> responseType = new ParameterizedTypeReference<>() {
        };
        var expectedEmail = TestDataFactory.buildEmail();
        var entity = new HttpEntity<>(expectedEmail, null);

        // When
        var responseEntity = oAuth2RestTemplate.exchange(emailServiceUrl, HttpMethod.POST, entity, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }
}
