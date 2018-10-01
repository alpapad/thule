package uk.co.serin.thule.email.service;

import uk.co.serin.thule.utils.aop.LogException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import uk.co.serin.thule.email.domain.Attachment;
import uk.co.serin.thule.email.domain.Email;

import java.util.concurrent.Future;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;

@Service
@LogException
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public Future<Email> createEmail(Email email) {
        if (!email.hasARecipient()) {
            throw new ValidationException("At least one recipient email addresses ('TO', 'CC' 'BCC') should be provided");
        }
        try {
            mailSender.send(createMimeMessage(email));
            LOGGER.info("Email has been successfully sent");
        } catch (Exception exception) {
            throw new EmailServiceException("Email could not be sent", exception);
        }
        return new AsyncResult<>(email);
    }

    private MimeMessage createMimeMessage(Email email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        if (!email.getBccs().isEmpty()) {
            mimeMessageHelper.setBcc(email.getBccs().toArray(new String[0]));
        }
        if (!email.getCcs().isEmpty()) {
            mimeMessageHelper.setCc(email.getCcs().toArray(new String[0]));
        }
        if (StringUtils.hasText(email.getFrom())) {
            mimeMessageHelper.setFrom(email.getFrom());
        }
        mimeMessageHelper.setSubject(email.getSubject());
        mimeMessageHelper.setText(email.getBody());
        if (!email.getTos().isEmpty()) {
            mimeMessageHelper.setTo(email.getTos().toArray(new String[0]));
        }
        // Add attachments
        for (Attachment attachment : email.getAttachments()) {
            mimeMessageHelper.addAttachment(attachment.getLabel(), new ByteArrayResource(attachment.getContent().getBytes()));
        }

        return mimeMessage;
    }
}
