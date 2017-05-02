package uk.co.serin.thule.email.domain;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AttachmentTest {
    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        byte[] content = "content".getBytes();
        String label = "label";

        // When
        Attachment attachment = new Attachment(content, label);

        // Then
        assertThat(attachment.getContent()).isEqualTo(content);
        assertThat(attachment.getLabel()).isEqualTo(label);
    }

    @Test
    public void copyConstructorCreatesInstanceWithSameFieldValues() {
        // Given
        byte[] content = "content".getBytes();
        String label = "label";

        Attachment expectedAttachment = new Attachment(content, label);

        // When
        Attachment actualAttachment = new Attachment(expectedAttachment);

        // Then
        assertThat(actualAttachment).isEqualToComparingFieldByField(expectedAttachment);
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        byte[] content = "content".getBytes();
        String label = "label";

        Attachment attachment = new Attachment(content, label);

        // When/Then
        assertThat(attachment.getContent()).isEqualTo(content);
        assertThat(attachment.getLabel()).isEqualTo(label);
    }

    @Test
    public void toStringIsOverridden() {
        // Given
        byte[] content = "content".getBytes();
        String label = "label";

        Attachment attachment = new Attachment(content, label);

        // When
        String attachmentAsString = attachment.toString();

        // Then
        assertThat(attachmentAsString).contains(new String(content)).contains(label);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Attachment.class).verify();
    }
}