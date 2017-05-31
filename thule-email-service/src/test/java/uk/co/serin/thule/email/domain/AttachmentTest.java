package uk.co.serin.thule.email.domain;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AttachmentTest {
    @Test
    public void builderAndGettersOperateOnTheSameField() {
        // Given
        Attachment expectedAttachment = newAttachment();

        // When
        Attachment actualAttachment = Attachment.AttachmentBuilder.anAttachment().
                withContent(expectedAttachment.getContent()).
                withLabel(expectedAttachment.getLabel()).build();

        // Then
        assertThat(actualAttachment.getContent()).isEqualTo(expectedAttachment.getContent());
        assertThat(actualAttachment.getLabel()).isEqualTo(expectedAttachment.getLabel());
    }

    private Attachment newAttachment() {
        return Attachment.AttachmentBuilder.anAttachment().withContent("content".getBytes()).withLabel("label").build();
    }

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        Attachment expectedAttachment = newAttachment();

        // When
        Attachment actualAttachment = new Attachment(expectedAttachment.getContent(), expectedAttachment.getLabel());

        // Then
        assertThat(actualAttachment.getContent()).isEqualTo(expectedAttachment.getContent());
        assertThat(actualAttachment.getLabel()).isEqualTo(expectedAttachment.getLabel());
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        Attachment expectedAttachment = newAttachment();

        // When
        Attachment actualAttachment = new Attachment(expectedAttachment.getContent(), expectedAttachment.getLabel());

        // Then
        assertThat(actualAttachment.getContent()).isEqualTo(expectedAttachment.getContent());
        assertThat(actualAttachment.getLabel()).isEqualTo(expectedAttachment.getLabel());
    }

    @Test
    public void toStringIsOverridden() {
        // Given
        Attachment attachment = newAttachment();

        // When
        String attachmentAsString = attachment.toString();

        // Then
        assertThat(attachmentAsString).contains(Attachment.ENTITY_ATTRIBUTE_NAME_LABEL);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Attachment.class).verify();
    }
}