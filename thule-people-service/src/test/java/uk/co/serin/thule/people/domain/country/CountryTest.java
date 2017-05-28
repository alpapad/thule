package uk.co.serin.thule.people.domain.country;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.MockReferenceDataFactory;
import uk.co.serin.thule.people.datafactories.ReferenceDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryTest {
    private ReferenceDataFactory referenceDataFactory = new MockReferenceDataFactory();

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given

        // When
        Country country = new Country(Country.GBR);

        // Then
        assertThat(country.getIsoCodeThreeDigit()).isEqualTo(Country.GBR);
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        Country country = new Country();

        // Then
        assertThat(country).isNotNull();
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        String isoCodeThreeDigit = "isoCodeThreeDigit";
        String isoCodeTwoDigit = "isoCodeTwoDigit";
        String isoName = "isoName";
        String isoNumber = "isoNumber";

        Country country = new Country(isoCodeThreeDigit).setIsoCodeTwoDigit(isoCodeTwoDigit).setIsoName(isoName).setIsoNumber(isoNumber);

        // When/Then
        assertThat(country.getIsoCodeThreeDigit()).isEqualTo(isoCodeThreeDigit);
        assertThat(country.getIsoCodeTwoDigit()).isEqualTo(isoCodeTwoDigit);
        assertThat(country.getIsoName()).isEqualTo(isoName);
        assertThat(country.getIsoNumber()).isEqualTo(isoNumber);
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new Country(Country.GBR).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_ISO_CODE_THREE_DIGIT);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Country.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}