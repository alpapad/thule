package uk.co.serin.thule.people.domain.address;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.MockReferenceDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.State;
import uk.co.serin.thule.people.domain.state.StateCode;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeAddressTest {
    private final MockReferenceDataFactory referenceDataFactory = new MockReferenceDataFactory();

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        String addressLine1 = "addressLine1";
        Country country = referenceDataFactory.getCountries().get(Country.GBR);
        String postCode = "postCode";

        // When
        HomeAddress homeAddress = new HomeAddress(addressLine1, postCode, country);

        // Then
        assertThat(homeAddress.getAddressLine1()).isEqualTo(addressLine1);
        assertThat(homeAddress.getCountry()).isEqualTo(country);
        assertThat(homeAddress.getPostCode()).isEqualTo(postCode);
    }

    @Test
    public void copyConstructorCreatesInstanceWithSameFieldValues() {
        // Given
        HomeAddress expectedHomeAddress = new HomeAddress("Oxford Street", "EC3", referenceDataFactory.getCountries().get(Country.GBR));
        expectedHomeAddress.setAddressLine2("Green");
        expectedHomeAddress.setCounty("Greater London");
        expectedHomeAddress.setState(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED));
        expectedHomeAddress.setTown("London");

        // When
        HomeAddress actualHomeAddress = new HomeAddress(expectedHomeAddress);

        // Then
        assertThat(actualHomeAddress).isEqualToComparingFieldByField(expectedHomeAddress);
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        HomeAddress homeAddress = new HomeAddress();

        // Then
        assertThat(homeAddress).isNotNull();
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        String addressLine1 = "addressLine1";
        String addressLine2 = "addressLine2";
        Country country = referenceDataFactory.getCountries().get(Country.GBR);
        String county = "county";
        String postCode = "postCode";
        State state = referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED);
        String town= "town";

        HomeAddress homeAddress = new HomeAddress(addressLine1, postCode, country);
        homeAddress.setAddressLine2(addressLine2);
        homeAddress.setCounty(county);
        homeAddress.setState(state);
        homeAddress.setTown(town);

        // When/Then
        assertThat(homeAddress.getAddressLine1()).isEqualTo(addressLine1);
        assertThat(homeAddress.getAddressLine2()).isEqualTo(addressLine2);
        assertThat(homeAddress.getCountry()).isEqualTo(country);
        assertThat(homeAddress.getCounty()).isEqualTo(county);
        assertThat(homeAddress.getPostCode()).isEqualTo(postCode);
        assertThat(homeAddress.getState()).isEqualTo(state);
        assertThat(homeAddress.getTown()).isEqualTo(town);
    }

    @Test
    public void toStringIsOverridden() {
        // Given
        String addressLine1 = "addressLine1";
        Country country = referenceDataFactory.getCountries().get(Country.GBR);
        String postCode = "postCode";

        // When
        HomeAddress homeAddress = new HomeAddress(addressLine1, postCode, country);

        // Then
        assertThat(homeAddress.toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_ADDRESS_LINE_1);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(HomeAddress.class).
                withPrefabValues(Action.class, new Action(ActionCode.ADDRESS_DISABLE), new Action(ActionCode.ADDRESS_DISCARD)).
                suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}