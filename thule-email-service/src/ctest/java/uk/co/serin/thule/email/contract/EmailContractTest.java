package uk.co.serin.thule.email.contract;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import uk.co.serin.thule.email.domain.model.Attachment;
import uk.co.serin.thule.email.domain.model.Email;
import uk.co.serin.thule.security.oauth2.Oauth2Utils;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.awaitility.Awaitility.await;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

public class EmailContractTest extends ContractBaseTest {
    private String baseUrl;
    private OAuth2RestTemplate oAuth2RestTemplate;
    private ParameterizedTypeReference<Email> responseType = new ParameterizedTypeReference<>() {
    };
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void given_an_smtp_server_that_is_down_when_sending_an_email_then_response_should_be_accepted() {
        // Given
        var attachments = Collections.singleton(Attachment.builder().content("This is another test attachment").label("test-attachment.txt").build());
        var email = Email.builder().attachments(attachments).bccs(Collections.singleton("bcc@test.co.uk")).body("This is another test body")
                         .ccs(Collections.singleton("ccs@test.co.uk")).from("from@test.co.uk").subject("Test subject")
                         .tos(Stream.of("to1@test.co.uk", "to2@test.co.uk", "to3@test.co.uk").collect(Collectors.toSet())).build();
        var entity = new HttpEntity<>(email, null);

        // When
        var responseEntity = oAuth2RestTemplate.exchange(baseUrl, HttpMethod.POST, entity, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Before
    public void setUp() {
        baseUrl = testRestTemplate.getRootUri() + "/emails";

        var jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(
                "username", "password", 0, Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId", "gmjtdvNVmQRz8bzw6ae");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));
    }

    @Test
    public void when_sending_an_email_then_the_email_is_sent_by_the_smtp_server() {
        // Given
        startEmbeddedSmtpServer();

        var attachments = Collections.singleton(Attachment.builder().content("This is a test attachment").label("test-attachment.txt").build());
        var email = Email.builder().attachments(attachments).bccs(Collections.singleton("bcc@test.co.uk")).body("This is a test body")
                         .ccs(Collections.singleton("ccs@test.co.uk")).from("from@test.co.uk").subject("Test subject")
                         .tos(Stream.of("to1@test.co.uk", "to2@test.co.uk", "to3@test.co.uk").collect(Collectors.toSet())).build();
        var expectedAttachment = email.getAttachments().stream().findFirst().orElseThrow();
        var httpEntity = new HttpEntity<>(email);

        // When
        var emailServiceResponse = oAuth2RestTemplate.exchange(baseUrl, HttpMethod.POST, httpEntity, responseType);

        // Then
        await().until(() -> getSmtpServer().getEmailCount() > 0);

        assertThat(emailServiceResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        var actualMailMessage = getSmtpServer().getMessage(0);
        assertThat(actualMailMessage.getBody()).contains(email.getBody());
        assertThat(actualMailMessage.getBody()).contains(expectedAttachment.getContent());
        assertThat(actualMailMessage.getFirstHeaderValue("From")).isEqualTo(email.getFrom());

        var expectedTos = email.getTos().toArray(new String[0]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[0]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[1]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[2]);
    }
}
