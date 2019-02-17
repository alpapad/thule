package uk.co.serin.thule.email.contract;

import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SmtpServerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uk.co.serin.thule.email.TestDataFactory;
import uk.co.serin.thule.email.domain.Email;
import uk.co.serin.thule.test.assertj.ActuatorUri;
import uk.co.serin.thule.utils.oauth2.Oauth2Utils;

import java.net.Socket;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;

import static org.awaitility.Awaitility.await;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ActiveProfiles("ctest")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailContractTest {
    private static final String SPRING_MAIL_HOST = "spring.mail.host";
    private static final String SPRING_MAIL_PORT = "spring.mail.port";
    private String emailServiceUrl;
    @Autowired
    private Environment env;
    private OAuth2RestTemplate oAuth2RestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private SmtpServer smtpServer;

    @Before
    public void setUp() {
        emailServiceUrl = String.format("http://localhost:%s/%s", port, Email.ENTITY_NAME_EMAILS);

        var jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(
                "username", "password", 0, Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId", "gmjtdvNVmQRz8bzw6ae");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));
    }

    @Test
    public void when_checking_health_then_status_is_up() {
        // Given
        stopAndStartEmbeddedSmtpServer();

        var actuatorUri = new ActuatorUri(URI.create(String.format("http://localhost:%s/actuator/health", port)));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    private void stopAndStartEmbeddedSmtpServer() {
        stopEmbeddedServer();

        var serverOptions = new ServerOptions();
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
        var expectedEmail = TestDataFactory.buildEmail();
        var expectedAttachment = expectedEmail.getAttachments().stream().findFirst().orElseThrow(() -> new IllegalStateException("Expected email does not contain any attachments"));
        var entity = new HttpEntity<>(expectedEmail, null);

        // When
        var emailServiceResponse = oAuth2RestTemplate.exchange(emailServiceUrl, HttpMethod.POST, entity, responseType);

        //Then
        await().until(() -> smtpServer.getEmailCount() > 0);
        assertThat(smtpServer.getEmailCount()).isEqualTo(1);

        assertThat(emailServiceResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        var actualMailMessage = smtpServer.getMessage(0);
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
        stopEmbeddedServer();

        ParameterizedTypeReference<Email> responseType = new ParameterizedTypeReference<>() {
        };
        var expectedEmail = TestDataFactory.buildEmail();
        var entity = new HttpEntity<>(expectedEmail, null);

        // When
        var responseEntity = oAuth2RestTemplate.exchange(emailServiceUrl, HttpMethod.POST, entity, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(isSmtpServerUp()).isFalse();
    }

    private boolean isSmtpServerUp() {
        try (var socket = new Socket(env.getRequiredProperty(SPRING_MAIL_HOST), env.getRequiredProperty(SPRING_MAIL_PORT, Integer.class))) {
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
            var serverOptions = new ServerOptions();
            serverOptions.port = smtpServerPort;
            return SmtpServerFactory.startServer(serverOptions);
        }
    }
}
