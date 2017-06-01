package uk.co.serin.thule.email.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.email.datafactories.TestDataFactory;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailTest {
    @Test
    public void builderAndGettersOperateOnTheSameField() {
        // Given
        Email expectedEmail = TestDataFactory.buildEmail();

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

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        Email expectedEmail = TestDataFactory.buildEmail();

        // When
        Email actualEmail = new Email(expectedEmail.getFrom(), expectedEmail.getSubject());

        // Then
        assertThat(actualEmail.getFrom()).isEqualTo(expectedEmail.getFrom());
        assertThat(actualEmail.getSubject()).isEqualTo(expectedEmail.getSubject());
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        Email email = new Email();

        // Then
        assertThat(email).isNotNull();
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        Email expectedEmail = TestDataFactory.buildEmail();

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
    public void has_a_recipient() {
        Email email = Email.EmailBuilder.anEmail().
                withBccs(Collections.singleton("bcc@test.co.uk")).
                withCcs(Collections.singleton("cc@test.co.uk")).
                withFrom("from@test.co.uk").
                withSubject("subject").
                withTos(Collections.singleton("to@test.co.uk")).
                build();

        // When/Then
        assertThat(email.hasARecipient()).isTrue();
    }

    @Test
    public void has_a_recipient_when_only_has_a_bcc() {
        // Given
        Email email = Email.EmailBuilder.anEmail().
                withBccs(Collections.singleton("bcc@test.co.uk")).
                withFrom("from@test.co.uk").
                withSubject("subject").
                build();

        // When/Then
        assertThat(email.hasARecipient()).isTrue();
    }

    @Test
    public void has_a_recipient_when_only_has_a_cc_and_bcc() {
        // Given
        Email email = Email.EmailBuilder.anEmail().
                withBccs(Collections.singleton("bcc@test.co.uk")).
                withCcs(Collections.singleton("cc@test.co.uk")).
                withFrom("from@test.co.uk").
                withSubject("subject").
                build();

        // When/Then
        assertThat(email.hasARecipient()).isTrue();
    }

    @Test
    public void has_no_recipients_when_tos_ccs_and_bccs_are_empty() {
        // Given
        Email expectedEmail = new Email("from@test.co.uk", "subject");

        // When
        Email actualEmail = new Email(expectedEmail.getFrom(), expectedEmail.getSubject());

        // Then
        assertThat(actualEmail.hasARecipient()).isFalse();
    }

    @Test
    public void toStringIsOverridden() {
        // Given
        Email email = TestDataFactory.buildEmail();

        // When
        String emailAsString = email.toString();

        // Then
        assertThat(emailAsString).contains(Email.ENTITY_ATTRIBUTE_NAME_FROM);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Email.class).
                suppress(Warning.NONFINAL_FIELDS).
                withOnlyTheseFields(Email.ENTITY_ATTRIBUTE_NAME_FROM, Email.ENTITY_ATTRIBUTE_NAME_SUBJECT).
                verify();
    }
}