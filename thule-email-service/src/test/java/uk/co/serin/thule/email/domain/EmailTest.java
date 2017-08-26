package uk.co.serin.thule.email.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import uk.co.serin.thule.email.datafactories.TestDataFactory;

import java.util.Collections;

import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class EmailTest {
    @Test
    public void builder_and_getters_operate_on_the_same_field() {
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

    @Test(expected = ValidationException.class)
    public void business_key_constructor_throws_ValidationException_when_from_is_empty() {
        // Given

        // When
        new Email("", "test subject");

        // Then (see expected in @Test annotation)
    }

    @Test(expected = ValidationException.class)
    public void business_key_constructor_throws_ValidationException_when_subject_is_empty() {
        // Given

        // When
        new Email("from@test.co.uk", "");

        // Then (see expected in @Test annotation)
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
    public void pojo_methods_are_well_implemented() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(Email.class, FieldPredicate.exclude("from", "subject", "attachments", "bccs", "ccs", "tos")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(Email.class).
                testing(Method.CONSTRUCTOR, Method.GETTER, Method.TO_STRING).areWellImplemented();
    }

    @Test
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Email.class).
                suppress(Warning.NONFINAL_FIELDS).
                withOnlyTheseFields(Email.ENTITY_ATTRIBUTE_NAME_FROM, Email.ENTITY_ATTRIBUTE_NAME_SUBJECT).
                verify();
    }
}