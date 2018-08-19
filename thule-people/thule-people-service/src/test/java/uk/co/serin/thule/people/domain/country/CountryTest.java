package uk.co.serin.thule.people.domain.country;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import uk.co.serin.thule.people.datafactory.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class CountryTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void when_builder_method_then_getters_operate_on_the_same_field() {
        // Given
        Country expectedCountry = testDataFactory.getCountries().get(Country.GBR);

        // When
        Country actualCountry = Country.CountryBuilder.aCountry().
                withCreatedAt(expectedCountry.getCreatedAt()).
                withCreatedBy(expectedCountry.getCreatedBy()).
                withId(expectedCountry.getId()).
                withIsoCodeThreeDigit(expectedCountry.getIsoCodeThreeDigit()).
                withIsoCodeTwoDigit(expectedCountry.getIsoCodeTwoDigit()).
                withIsoName(expectedCountry.getIsoName()).
                withIsoNumber(expectedCountry.getIsoNumber()).
                withIsoCodeTwoDigit(expectedCountry.getIsoCodeTwoDigit()).
                withUpdatedAt(expectedCountry.getUpdatedAt()).
                withUpdatedBy(expectedCountry.getUpdatedBy()).
                withVersion(expectedCountry.getVersion()).
                build();

        // Then
        assertThat(actualCountry.getCreatedAt()).isEqualTo(expectedCountry.getCreatedAt());
        assertThat(actualCountry.getCreatedBy()).isEqualTo(expectedCountry.getCreatedBy());
        assertThat(actualCountry.getId()).isEqualTo(expectedCountry.getId());
        assertThat(actualCountry.getIsoCodeThreeDigit()).isEqualTo(expectedCountry.getIsoCodeThreeDigit());
        assertThat(actualCountry.getIsoCodeTwoDigit()).isEqualTo(expectedCountry.getIsoCodeTwoDigit());
        assertThat(actualCountry.getIsoName()).isEqualTo(expectedCountry.getIsoName());
        assertThat(actualCountry.getIsoNumber()).isEqualTo(expectedCountry.getIsoNumber());
        assertThat(actualCountry.getUpdatedAt()).isEqualTo(expectedCountry.getUpdatedAt());
        assertThat(actualCountry.getUpdatedBy()).isEqualTo(expectedCountry.getUpdatedBy());
        assertThat(actualCountry.getVersion()).isEqualTo(expectedCountry.getVersion());
    }

    @Test
    public void when_equals_is_overridden_then_verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Country.class).withOnlyTheseFields(Country.ENTITY_ATTRIBUTE_NAME_ISO_CODE_THREE_DIGIT).verify();
    }

    @Test
    public void when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(Country.class, FieldPredicate.exclude("isoCodeThreeDigit")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(Country.class).
                testing(Method.CONSTRUCTOR, Method.GETTER, Method.TO_STRING).areWellImplemented();
    }
}