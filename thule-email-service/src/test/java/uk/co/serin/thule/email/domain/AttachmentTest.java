package uk.co.serin.thule.email.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import pl.pojo.tester.api.assertion.Method;

import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class AttachmentTest {
    @Test(expected = ValidationException.class)
    public void business_key_constructor_throws_ValidationException_when_content_is_empty() {
        // Given

        // When
        new Attachment("", "test label");

        // Then (see expected in @Test annotation)
    }

    @Test(expected = ValidationException.class)
    public void business_key_constructor_throws_ValidationException_when_label_is_empty() {
        // Given

        // When
        new Attachment("test content", "");

        // Then (see expected in @Test annotation)
    }

    @Test
    public void pojo_methods_are_well_implemented() {
        // Given
        Object[] constructorParameters = {"test content", "test label"};
        Class[] constructorParameterTypes = {String.class, String.class};

        // When
        Throwable throwable = Assertions.catchThrowable(() ->
                assertPojoMethodsFor(Attachment.class).
                        create(Attachment.class, constructorParameters, constructorParameterTypes).
                        testing(Method.CONSTRUCTOR, Method.GETTER, Method.TO_STRING).
                        areWellImplemented());

        // Then
        assertThat(throwable).isNull();
    }

    @Test
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Attachment.class).
                suppress(Warning.NONFINAL_FIELDS).
                withOnlyTheseFields(Attachment.ENTITY_ATTRIBUTE_NAME_CONTENT, Attachment.ENTITY_ATTRIBUTE_NAME_LABEL).
                verify();
    }
}