package uk.co.serin.thule.email.contract;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.reactive.server.WebTestClient;

import uk.co.serin.thule.email.domain.model.Attachment;
import uk.co.serin.thule.email.domain.model.Email;
import uk.co.serin.thule.resourceserver.utils.JwtUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.awaitility.Awaitility.await;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

public class EmailContractTest extends ContractBaseTest {
    @Autowired
    private WebTestClient webTestClient;

    @Before
    public void before() {
        var jwt = JwtUtils.createKeycloakJwt("username", 0, Set.of(new SimpleGrantedAuthority("grantedAuthority")), "clientId");
        webTestClient = webTestClient.mutate().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue()).build();
    }

    @Test
    public void given_an_smtp_server_that_is_down_when_sending_an_email_then_response_should_be_accepted() {
        // Given
        var attachments = Set.of(Attachment.builder().content("This is another test attachment").label("test-attachment.txt").build());
        var email = Email.builder().attachments(attachments).bccs(Set.of("bcc@test.co.uk")).body("This is another test body")
                         .ccs(Set.of("ccs@test.co.uk")).from("from@test.co.uk").subject("Test subject")
                         .tos(Stream.of("to1@test.co.uk", "to2@test.co.uk", "to3@test.co.uk").collect(Collectors.toSet())).build();

        // When
        webTestClient.post().uri("/emails")
                     .bodyValue(email)
                     .exchange()
                     // Then
                     .expectStatus().isAccepted();
    }

    @Test
    public void when_sending_an_email_then_the_email_is_sent_by_the_smtp_server() {
        // Given
        startEmbeddedSmtpServer();

        var attachments = Set.of(Attachment.builder().content("This is a test attachment").label("test-attachment.txt").build());
        var email = Email.builder().attachments(attachments).bccs(Set.of("bcc@test.co.uk")).body("This is a test body")
                         .ccs(Set.of("ccs@test.co.uk")).from("from@test.co.uk").subject("Test subject")
                         .tos(Stream.of("to1@test.co.uk", "to2@test.co.uk", "to3@test.co.uk").collect(Collectors.toSet())).build();
        var expectedAttachment = email.getAttachments().stream().findFirst().orElseThrow();

        // When
        webTestClient.post().uri("/emails")
                     .bodyValue(email)
                     .exchange()

                     // Then
                     .expectStatus().isAccepted();

        await().until(() -> Arrays.stream(getSmtpServer().getMessages()).anyMatch(mailMessage -> mailMessage.getBody().contains(email.getBody())));

        var actualMailMessageOptional =
                Arrays.stream(getSmtpServer().getMessages()).filter(mailMessage -> mailMessage.getBody().contains(email.getBody())).findFirst();
        assertThat(actualMailMessageOptional).isNotEmpty();
        var actualMailMessage = actualMailMessageOptional.orElseThrow();
        assertThat(actualMailMessage.getBody()).contains(expectedAttachment.getContent());
        assertThat(actualMailMessage.getFirstHeaderValue("From")).isEqualTo(email.getFrom());

        var expectedTos = email.getTos().toArray(new String[0]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[0]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[1]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[2]);
    }
}
