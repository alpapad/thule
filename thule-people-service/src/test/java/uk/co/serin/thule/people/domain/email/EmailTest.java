package uk.co.serin.thule.people.domain.email;

import org.junit.Test;

import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import uk.co.serin.thule.people.datafactory.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

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
    public void pojo_methods_are_well_implemented() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(Email.class, FieldPredicate.exclude("subject", "tos")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(Email.class).
                testing(Method.CONSTRUCTOR, Method.GETTER, Method.TO_STRING).areWellImplemented();
    }
}