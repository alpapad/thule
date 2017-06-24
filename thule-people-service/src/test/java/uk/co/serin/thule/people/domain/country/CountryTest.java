package uk.co.serin.thule.people.domain.country;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builder_and_getters_operate_on_the_same_field() {
        // Given
        Country expectedCountry = testDataFactory.getCountries().get(Country.GBR);

        // When
        Country actualCountry = Country.CountryBuilder.aCountry().
                withCreatedAt(expectedCountry.getCreatedAt()).
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
    public void business_key_constructor_creates_instance_with_correct_key() {
        // Given

        // When
        Country country = new Country(Country.GBR);

        // Then
        assertThat(country.getIsoCodeThreeDigit()).isEqualTo(Country.GBR);
    }

    @Test
    public void default_constructor_creates_instance_successfully() {
        // Given

        // When
        Country country = new Country();

        // Then
        assertThat(country).isNotNull();
    }

    @Test
    public void getters_and_setters_operate_on_the_same_field() {
        // Given
        Country expectedCountry = testDataFactory.getCountries().get(Country.GBR);

        // When
        Country actualCountry = new Country(expectedCountry.getIsoCodeThreeDigit());
        actualCountry.setIsoCodeTwoDigit(expectedCountry.getIsoCodeTwoDigit());
        actualCountry.setIsoName(expectedCountry.getIsoName());
        actualCountry.setIsoNumber(expectedCountry.getIsoNumber());

        // Then
        assertThat(actualCountry.getCreatedAt()).isEqualTo(expectedCountry.getCreatedAt());
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
    public void toString_is_overridden() {
        assertThat(new Country(Country.GBR).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_ISO_CODE_THREE_DIGIT);
    }

    @Test
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Country.class).withOnlyTheseFields(Country.ENTITY_ATTRIBUTE_NAME_ISO_CODE_THREE_DIGIT).verify();
    }
}