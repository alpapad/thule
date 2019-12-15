package uk.co.serin.thule.email.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import uk.co.serin.thule.email.domain.model.Email;
import uk.co.serin.thule.utils.trace.LogException;
import uk.co.serin.thule.utils.trace.TracePublicMethods;

import java.util.concurrent.Future;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@TracePublicMethods
@Slf4j
public class EmailService {
    @NonNull
    private JavaMailSender mailSender;

    @Async
    @LogException
    public Future<Email> send(Email email) {
        if (!hasARecipient(email)) {
            throw new ValidationException("At least one recipient email addresses ('TO', 'CC' 'BCC') should be provided");
        }
        try {
            mailSender.send(createMimeMessage(email));
            log.info("Email has been successfully sent");
        } catch (Exception exception) {
            throw new EmailServiceException("Email could not be sent", exception);
        }
        return new AsyncResult<>(email);
    }

    private boolean hasARecipient(Email email) {
        return !email.getTos().isEmpty() || !email.getCcs().isEmpty() || !email.getBccs().isEmpty();
    }

    private MimeMessage createMimeMessage(Email email) throws MessagingException {
        var mimeMessage = mailSender.createMimeMessage();
        var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

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
        for (var attachment : email.getAttachments()) {
            mimeMessageHelper.addAttachment(attachment.getLabel(), new ByteArrayResource(attachment.getContent().getBytes()));
        }

        return mimeMessage;
    }
}
