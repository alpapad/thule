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

public class WorkAddressTest {
    private final MockReferenceDataFactory referenceDataFactory = new MockReferenceDataFactory();

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        String addressLine1 = "addressLine1";
        Country country = referenceDataFactory.getCountries().get(Country.GBR);
        String postCode = "postCode";

        // When
        WorkAddress workAddress = new WorkAddress(addressLine1, postCode, country);

        // Then
        assertThat(workAddress.getAddressLine1()).isEqualTo(addressLine1);
        assertThat(workAddress.getCountry()).isEqualTo(country);
        assertThat(workAddress.getPostCode()).isEqualTo(postCode);
    }

    @Test
    public void copyConstructorCreatesInstanceWithSameFieldValues() {
        // Given
        WorkAddress expectedWorkAddress = new WorkAddress("Regent Street", "EC4", referenceDataFactory.getCountries().get(Country.GBR));
        expectedWorkAddress.setAddressLine2("Green");
        expectedWorkAddress.setCounty("Greater London");
        expectedWorkAddress.setState(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED));
        expectedWorkAddress.setTown("London");

        // When
        WorkAddress actualWorkAddress = new WorkAddress(expectedWorkAddress);

        // Then
        assertThat(actualWorkAddress).isEqualToComparingFieldByField(expectedWorkAddress);
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        WorkAddress workAddress = new WorkAddress();

        // Then
        assertThat(workAddress).isNotNull();
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
        String town = "town";

        WorkAddress workAddress = new WorkAddress(addressLine1, postCode, country);
        workAddress.setAddressLine2(addressLine2);
        workAddress.setCounty(county);
        workAddress.setState(state);
        workAddress.setTown(town);

        // When/Then
        assertThat(workAddress.getAddressLine1()).isEqualTo(addressLine1);
        assertThat(workAddress.getAddressLine2()).isEqualTo(addressLine2);
        assertThat(workAddress.getCountry()).isEqualTo(country);
        assertThat(workAddress.getCounty()).isEqualTo(county);
        assertThat(workAddress.getPostCode()).isEqualTo(postCode);
        assertThat(workAddress.getState()).isEqualTo(state);
        assertThat(workAddress.getTown()).isEqualTo(town);
    }

    @Test
    public void toStringIsOverridden() {
        // Given
        String addressLine1 = "addressLine1";
        Country country = referenceDataFactory.getCountries().get(Country.GBR);
        String postCode = "postCode";

        // When
        WorkAddress workAddress = new WorkAddress(addressLine1, postCode, country);

        // Then
        assertThat(workAddress.toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_ADDRESS_LINE_1);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(WorkAddress.class).
                withPrefabValues(Action.class, new Action(ActionCode.ADDRESS_DISABLE), new Action(ActionCode.ADDRESS_DISCARD)).
                suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}