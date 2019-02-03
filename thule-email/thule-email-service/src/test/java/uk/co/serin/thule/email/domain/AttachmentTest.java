package uk.co.serin.thule.email.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import pl.pojo.tester.api.assertion.Method;

import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class AttachmentTest {
    @Test
    public void when_content_is_empty_then_business_key_constructor_throws_a_validation_exception() {
        // Given

        // When
        Throwable throwable = catchThrowable(() -> new Attachment("", "test label"));

        // Then
        assertThat(throwable).isInstanceOf(ValidationException.class);
    }

    @Test
    public void when_equals_is_overridden_then_verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Attachment.class).
                suppress(Warning.NONFINAL_FIELDS).
                              withOnlyTheseFields(Attachment.ENTITY_ATTRIBUTE_NAME_CONTENT, Attachment.ENTITY_ATTRIBUTE_NAME_LABEL).
                              verify();
    }

    @Test
    public void when_label_is_empty_then_business_key_constructor_throws_a_validation_exception() {
        // Given

        // When
        Throwable throwable = catchThrowable(() -> new Attachment("test content", ""));

        // Then
        assertThat(throwable).isInstanceOf(ValidationException.class);
    }

    @Test
    public void when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
        // Given
        Object[] constructorParameters = {"test content", "test label"};
        Class[] constructorParameterTypes = {String.class, String.class};

        // When
        Throwable throwable = catchThrowable(() ->
                assertPojoMethodsFor(Attachment.class).
                                                              create(Attachment.class, constructorParameters, constructorParameterTypes).
                                                              testing(Method.CONSTRUCTOR, Method.GETTER, Method.TO_STRING).
                                                              areWellImplemented());

        // Then
        assertThat(throwable).isNull();
    }
}