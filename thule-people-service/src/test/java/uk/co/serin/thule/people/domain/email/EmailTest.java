package uk.co.serin.thule.people.domain.email;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builder_and_getters_operate_on_the_same_field() {
        // Given
        Email expectedEmail = testDataFactory.buildEmail();

        // When
        Email actualEmail = Email.EmailBuilder.anEmail().
                withBody(expectedEmail.getBody()).
                withSubject(expectedEmail.getSubject()).
                withTos(expectedEmail.getTos()).build();

        // Then
        assertThat(actualEmail.getBody()).isEqualTo(expectedEmail.getBody());
        assertThat(actualEmail.getSubject()).isEqualTo(expectedEmail.getSubject());
        assertThat(actualEmail.getTos()).isEqualTo(expectedEmail.getTos());
    }

    @Test
    public void default_constructor_creates_instance_successfully() {
        // Given

        // When
        Email email = new Email();

        // Then
        assertThat(email).isNotNull();
    }

    @Test
    public void getters_and_setters_operate_on_the_same_field() {
        // Given
        Email expectedEmail = testDataFactory.buildEmail();

        // When
        Email actualEmail = new Email();
        actualEmail.setBody(expectedEmail.getBody());
        actualEmail.setSubject(expectedEmail.getSubject());
        actualEmail.addTos(expectedEmail.getTos());

        // Then
        assertThat(actualEmail.getBody()).isEqualTo(expectedEmail.getBody());
        assertThat(actualEmail.getSubject()).isEqualTo(expectedEmail.getSubject());
        assertThat(actualEmail.getTos()).isEqualTo(expectedEmail.getTos());
    }

    @Test
    public void toString_is_overridden() {
        // Given
        Email email = testDataFactory.buildEmail();

        // When
        String emailAsString = email.toString();

        // Then
        assertThat(emailAsString).contains(Email.ENTITY_ATTRIBUTE_NAME_SUBJECT);
    }
}