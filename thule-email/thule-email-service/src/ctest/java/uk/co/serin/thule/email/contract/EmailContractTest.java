package uk.co.serin.thule.email.contract;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SmtpServerFactory;
import com.gohenry.test.assertj.ActuatorUri;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.email.datafactories.TestDataFactory;
import uk.co.serin.thule.email.domain.Attachment;
import uk.co.serin.thule.email.domain.Email;

import java.net.Socket;
import java.net.URI;
import java.time.Duration;

import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;
import static org.awaitility.Awaitility.await;

@ActiveProfiles("ctest")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailContractTest {
    private static final String SPRING_MAIL_HOST = "spring.mail.host";
    private static final String SPRING_MAIL_PORT = "spring.mail.port";
    private String emailServiceUrl = "/" + Email.ENTITY_NAME_EMAILS;
    @Autowired
    private Environment env;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SmtpServer smtpServer;

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        stopAndStartEmbeddedSmtpServer();

        ActuatorUri actuatorUri = new ActuatorUri(URI.create(restTemplate.getRootUri() + "/actuator/health"));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    private void stopAndStartEmbeddedSmtpServer() {
        stopEmbeddedServer();

        ServerOptions serverOptions = new ServerOptions();
        serverOptions.port = env.getRequiredProperty(SPRING_MAIL_PORT, Integer.class);
        smtpServer = SmtpServerFactory.startServer(serverOptions);
    }

    @After
    public void stopEmbeddedServer() {
        if (null != smtpServer) {
            smtpServer.stop();
            smtpServer = null;
        }
    }

    @Test
    public void when_sending_an_email_then_email_is_sent_by_the_smtp_server() {
        //Given
        stopAndStartEmbeddedSmtpServer();
        ParameterizedTypeReference<Email> responseType = new ParameterizedTypeReference<>() {
        };
        Email expectedEmail = TestDataFactory.buildEmail();
        Attachment expectedAttachment = expectedEmail.getAttachments().stream().findFirst().orElseThrow(() -> new IllegalStateException("Expected email does not contain any attachments"));

        HttpEntity<Email> entity = new HttpEntity<>(expectedEmail, null);

        // When
        ResponseEntity<Email> emailServiceResponse = restTemplate.exchange(emailServiceUrl, HttpMethod.POST, entity, responseType);

        //Then
        await().until(() -> smtpServer.getEmailCount() > 0);
        assertThat(smtpServer.getEmailCount()).isEqualTo(1);

        assertThat(emailServiceResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        MailMessage actualMailMessage = smtpServer.getMessage(0);
        assertThat(actualMailMessage.getBody()).contains(expectedEmail.getBody());
        assertThat(actualMailMessage.getBody()).contains(expectedAttachment.getContent());
        assertThat(actualMailMessage.getFirstHeaderValue("From")).isEqualTo(expectedEmail.getFrom());
        String[] expectedTos = expectedEmail.getTos().toArray(new String[0]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[0]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[1]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[2]);
    }

    @Test
    public void when_smtp_server_is_down_then_response_should_be_accepted() {
        //Given
        stopEmbeddedServer();
        ParameterizedTypeReference<Email> responseType = new ParameterizedTypeReference<>() {
        };
        Email expectedEmail = TestDataFactory.buildEmail();
        HttpEntity<Email> entity = new HttpEntity<>(expectedEmail, null);

        // When
        ResponseEntity<Email> responseEntity = restTemplate.exchange(emailServiceUrl, HttpMethod.POST, entity, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(isSmtpServerUp()).isFalse();
    }

    private boolean isSmtpServerUp() {
        try (Socket socket = new Socket(env.getRequiredProperty(SPRING_MAIL_HOST), env.getRequiredProperty(SPRING_MAIL_PORT, Integer.class))) {
            return socket.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    @TestConfiguration
    static class EmailIntTestConfiguration {
        @Value("${spring.mail.port}")
        private int smtpServerPort;

        @Bean
        public SmtpServer smtpServer() {
            ServerOptions serverOptions = new ServerOptions();
            serverOptions.port = smtpServerPort;
            return SmtpServerFactory.startServer(serverOptions);
        }
    }
}
