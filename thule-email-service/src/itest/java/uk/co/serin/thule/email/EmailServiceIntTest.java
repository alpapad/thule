package uk.co.serin.thule.email;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SmtpServerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.co.serin.thule.email.datafactories.TestDataFactory;
import uk.co.serin.thule.email.domain.Attachment;
import uk.co.serin.thule.email.domain.Email;

import java.net.Socket;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:/config/${spring.application.name}/"})
@ActiveProfiles("itest")
@RunWith(SpringJUnit4ClassRunner.class)
public class EmailServiceIntTest {
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
    public void response_should_be_accepted_when_the_smtp_server_is_down() {
        //Given
        stopEmbeddedServer();
        ParameterizedTypeReference<Email> responseType = new ParameterizedTypeReference<Email>() {
        };
        Email expectedEmail = TestDataFactory.buildEmail();
        HttpEntity<Email> entity = new HttpEntity<>(expectedEmail, null);

        // When
        ResponseEntity<Email> responseEntity = restTemplate.exchange(emailServiceUrl, HttpMethod.POST, entity, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(isSmtpServerUp()).isFalse();
    }

    @After
    public void stopEmbeddedServer() {
        if (null != smtpServer) {
            smtpServer.stop();
            smtpServer = null;
        }
    }

    private boolean isSmtpServerUp() {
        try (Socket socket = new Socket(env.getRequiredProperty(SPRING_MAIL_HOST), env.getRequiredProperty(SPRING_MAIL_PORT, Integer.class))) {
            return socket.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void send_an_email_results_in_the_email_being_sent_by_the_smtp_server() {
        //Given
        stopAndStartEmbeddedSmtpServer();
        ParameterizedTypeReference<Email> responseType = new ParameterizedTypeReference<Email>() {
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
        assertThat(actualMailMessage.getBody()).contains(new String(expectedAttachment.getContent()));
        assertThat(actualMailMessage.getFirstHeaderValue("From")).isEqualTo(expectedEmail.getFrom());
        String[] expectedTos = expectedEmail.getTos().toArray(new String[expectedEmail.getTos().size()]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[0]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[1]);
        assertThat(actualMailMessage.getFirstHeaderValue("To")).contains(expectedTos[2]);
    }

    private void stopAndStartEmbeddedSmtpServer() {
        stopEmbeddedServer();

        ServerOptions serverOptions = new ServerOptions();
        serverOptions.port = env.getRequiredProperty(SPRING_MAIL_PORT, Integer.class);
        smtpServer = SmtpServerFactory.startServer(serverOptions);
    }

    @Before
    public void setUp() {
        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
        ObjectMapper objectMapper = jackson2ObjectMapperBuilder.createXmlMapper(false).build();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Enforce java.time objects objects to be serialized in ISO-8601 format

        MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();
        httpMessageConverter.setObjectMapper(objectMapper);

        restTemplate.getRestTemplate().setMessageConverters(Collections.singletonList(httpMessageConverter));
    }
}
