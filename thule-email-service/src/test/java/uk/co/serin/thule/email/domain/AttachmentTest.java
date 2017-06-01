package uk.co.serin.thule.email.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.email.datafactories.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class AttachmentTest {
    @Test
    public void builderAndGettersOperateOnTheSameField() {
        // Given
        Attachment expectedAttachment = TestDataFactory.buildAttachment();

        // When
        Attachment actualAttachment = Attachment.AttachmentBuilder.anAttachment().
                withContent(expectedAttachment.getContent()).
                withLabel(expectedAttachment.getLabel()).build();

        // Then
        assertThat(actualAttachment.getContent()).isEqualTo(expectedAttachment.getContent());
        assertThat(actualAttachment.getLabel()).isEqualTo(expectedAttachment.getLabel());
    }

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        Attachment expectedAttachment = TestDataFactory.buildAttachment();

        // When
        Attachment actualAttachment = new Attachment(expectedAttachment.getContent(), expectedAttachment.getLabel());

        // Then
        assertThat(actualAttachment.getContent()).isEqualTo(expectedAttachment.getContent());
        assertThat(actualAttachment.getLabel()).isEqualTo(expectedAttachment.getLabel());
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        Attachment attachment = new Attachment();

        // Then
        assertThat(attachment).isNotNull();
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        Attachment expectedAttachment = TestDataFactory.buildAttachment();

        // When
        Attachment actualAttachment = new Attachment(expectedAttachment.getContent(), expectedAttachment.getLabel());

        // Then
        assertThat(actualAttachment.getContent()).isEqualTo(expectedAttachment.getContent());
        assertThat(actualAttachment.getLabel()).isEqualTo(expectedAttachment.getLabel());
    }

    @Test
    public void toStringIsOverridden() {
        // Given
        Attachment attachment = TestDataFactory.buildAttachment();

        // When
        String attachmentAsString = attachment.toString();

        // Then
        assertThat(attachmentAsString).contains(Attachment.ENTITY_ATTRIBUTE_NAME_LABEL);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Attachment.class).
                suppress(Warning.NONFINAL_FIELDS).
                withOnlyTheseFields(Attachment.ENTITY_ATTRIBUTE_NAME_CONTENT, Attachment.ENTITY_ATTRIBUTE_NAME_LABEL).
                verify();
    }
}