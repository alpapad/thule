package uk.co.serin.thule.email.domain;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailTest {
    @Test
    public void builderAndGettersOperateOnTheSameField() {
        // Given
        Email expectedEmail = newEmail();

        // When
        Email actualEmail = Email.EmailBuilder.anEmail().
                withAttachments(expectedEmail.getAttachments()).
                withBccs(expectedEmail.getBccs()).
                withBody(expectedEmail.getBody()).
                withCcs(expectedEmail.getCcs()).
                withFrom(expectedEmail.getFrom()).
                withSubject(expectedEmail.getSubject()).
                withTos(expectedEmail.getTos()).build();

        // Then
        assertThat(actualEmail.getAttachments()).isEqualTo(expectedEmail.getAttachments());
        assertThat(actualEmail.getBccs()).isEqualTo(expectedEmail.getBccs());
        assertThat(actualEmail.getBody()).isEqualTo(expectedEmail.getBody());
        assertThat(actualEmail.getCcs()).isEqualTo(expectedEmail.getCcs());
        assertThat(actualEmail.getFrom()).isEqualTo(expectedEmail.getFrom());
        assertThat(actualEmail.getSubject()).isEqualTo(expectedEmail.getSubject());
        assertThat(actualEmail.getTos()).isEqualTo(expectedEmail.getTos());
    }

    private Email newEmail() {
        Attachment attachment = new Attachment("content".getBytes(), "label");

        return Email.EmailBuilder.anEmail().
                withAttachments(Collections.singleton(attachment)).
                withBccs(Collections.singleton("bcc")).
                withBody("body").
                withCcs(Collections.singleton("ccs")).
                withFrom("from@emailaddress.co.uk").
                withSubject("subject").
                withTos(Collections.singleton("tos")).
                build();
    }

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        Email expectedEmail = newEmail();

        // When
        Email actualEmail = new Email(expectedEmail.getFrom(), expectedEmail.getSubject());

        // Then
        assertThat(actualEmail.getFrom()).isEqualTo(expectedEmail.getFrom());
        assertThat(actualEmail.getSubject()).isEqualTo(expectedEmail.getSubject());
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        Email expectedEmail = newEmail();

        // When
        Email actualEmail = new Email(expectedEmail.getFrom(), expectedEmail.getSubject());
        actualEmail.setBody(expectedEmail.getBody());
        actualEmail.addAttachments(expectedEmail.getAttachments());
        actualEmail.addBccs(expectedEmail.getBccs());
        actualEmail.addCcs(expectedEmail.getCcs());
        actualEmail.addTos(expectedEmail.getTos());

        // Then
        assertThat(actualEmail.getAttachments()).isEqualTo(expectedEmail.getAttachments());
        assertThat(actualEmail.getBccs()).isEqualTo(expectedEmail.getBccs());
        assertThat(actualEmail.getBody()).isEqualTo(expectedEmail.getBody());
        assertThat(actualEmail.getCcs()).isEqualTo(expectedEmail.getCcs());
        assertThat(actualEmail.getFrom()).isEqualTo(expectedEmail.getFrom());
        assertThat(actualEmail.getSubject()).isEqualTo(expectedEmail.getSubject());
        assertThat(actualEmail.getTos()).isEqualTo(expectedEmail.getTos());
    }

    @Test
    public void toStringIsOverridden() {
        // Given
        Email email = newEmail();

        // When
        String emailAsString = email.toString();

        // Then
        assertThat(emailAsString).contains(Email.ENTITY_ATTRIBUTE_NAME_FROM);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Email.class).withOnlyTheseFields(Email.ENTITY_ATTRIBUTE_NAME_FROM, Email.ENTITY_ATTRIBUTE_NAME_SUBJECT).verify();
    }
}