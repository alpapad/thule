package uk.co.serin.thule.email.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailTest {
    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        String from = "from@emailaddress.co.uk";
        String subject = "subject";

        // When
        Email email = new Email(from, subject);

        // Then
        assertThat(email.getFrom()).isEqualTo(from);
        assertThat(email.getSubject()).isEqualTo(subject);
    }

    @Test
    public void copyConstructorCreatesInstanceWithSameFieldValues() {
        // Given
        String from = "from@emailaddress.co.uk";
        String subject = "subject";

        Email expectedEmail = new Email(from, subject);

        // When
        Email actualEmail = new Email(expectedEmail);

        // Then
        assertThat(actualEmail).isEqualToComparingFieldByField(expectedEmail);
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        String from = "from@emailaddress.co.uk";
        String subject = "subject";
        Attachment attachment = new Attachment("content".getBytes(), "label");
        List<Attachment> attachments = Collections.singletonList(attachment);
        List<String> bccs = Collections.singletonList("bcc");
        String body = "body";
        List<String> ccs = Collections.singletonList("ccs");
        List<String> tos = Collections.singletonList("tos");

        Email email = new Email(from, subject);
        email.addAttachments(attachments.stream());
        email.addBccs(bccs.stream());
        email.setBody(body);
        email.addCcs(ccs.stream());
        email.addTos(tos.stream());

        // When/Then
        assertThat(email.getAttachments()).isEqualTo(attachments);
        assertThat(email.getBccs()).isEqualTo(bccs);
        assertThat(email.getBody()).isEqualTo(body);
        assertThat(email.getCcs()).isEqualTo(ccs);
        assertThat(email.getTos()).isEqualTo(tos);
    }

    @Test
    public void toStringIsOverridden() {
        String from = "from@emailaddress.co.uk";
        String subject = "subject";

        Email email = new Email(from, subject);

        // When
        String emailAsString = email.toString();

        // Then
        assertThat(emailAsString).contains(from).contains(subject);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Email.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}