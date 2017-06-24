package uk.co.serin.thule.email.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.email.datafactories.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class AttachmentTest {
    @Test
    public void builder_and_getters_operate_on_the_same_field() {
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
    public void business_key_constructor_creates_instance_with_correct_key() {
        // Given
        Attachment expectedAttachment = TestDataFactory.buildAttachment();

        // When
        Attachment actualAttachment = new Attachment(expectedAttachment.getContent(), expectedAttachment.getLabel());

        // Then
        assertThat(actualAttachment.getContent()).isEqualTo(expectedAttachment.getContent());
        assertThat(actualAttachment.getLabel()).isEqualTo(expectedAttachment.getLabel());
    }

    @Test
    public void default_constructor_creates_instance_successfully() {
        // Given

        // When
        Attachment attachment = new Attachment();

        // Then
        assertThat(attachment).isNotNull();
    }

    @Test
    public void getters_and_setters_operate_on_the_same_field() {
        // Given
        Attachment expectedAttachment = TestDataFactory.buildAttachment();

        // When
        Attachment actualAttachment = new Attachment(expectedAttachment.getContent(), expectedAttachment.getLabel());

        // Then
        assertThat(actualAttachment.getContent()).isEqualTo(expectedAttachment.getContent());
        assertThat(actualAttachment.getLabel()).isEqualTo(expectedAttachment.getLabel());
    }

    @Test
    public void toString_is_overridden() {
        // Given
        Attachment attachment = TestDataFactory.buildAttachment();

        // When
        String attachmentAsString = attachment.toString();

        // Then
        assertThat(attachmentAsString).contains(Attachment.ENTITY_ATTRIBUTE_NAME_LABEL);
    }

    @Test
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Attachment.class).
                suppress(Warning.NONFINAL_FIELDS).
                withOnlyTheseFields(Attachment.ENTITY_ATTRIBUTE_NAME_CONTENT, Attachment.ENTITY_ATTRIBUTE_NAME_LABEL).
                verify();
    }
}