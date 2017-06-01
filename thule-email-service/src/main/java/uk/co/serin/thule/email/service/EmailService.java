package uk.co.serin.thule.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import uk.co.serin.thule.email.domain.Email;

import java.util.concurrent.Future;

@Service
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private MailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async

    public Future<Email> createEmail(Email email) {
        if (!email.hasARecipient()) {
            throw new EmailServiceValidationException("At least one recipient email addresses ('TO', 'CC' 'BCC') should be provided");
        }
        try {
            SimpleMailMessage simpleMailMessage = createSimpleMailMessage(email);
            mailSender.send(simpleMailMessage);
            LOGGER.info("Mail has been successfully sent");
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
            throw new EmailServiceException(exception.getMessage(), exception);
        }
        return new AsyncResult<>(email);
    }

    private SimpleMailMessage createSimpleMailMessage(Email email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(email.getFrom());
        if (!email.getTos().isEmpty()) {
            simpleMailMessage.setTo(email.getTos().toArray(new String[email.getTos().size()]));
        }
        if (!email.getCcs().isEmpty()) {
            simpleMailMessage.setCc(email.getCcs().toArray(new String[email.getCcs().size()]));
        }
        if (!email.getBccs().isEmpty()) {
            simpleMailMessage.setBcc(email.getBccs().toArray(new String[email.getBccs().size()]));
        }
        simpleMailMessage.setSubject(email.getSubject());
        simpleMailMessage.setText(email.getBody());
        return simpleMailMessage;
    }
}
