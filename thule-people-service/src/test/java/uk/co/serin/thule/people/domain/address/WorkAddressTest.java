package uk.co.serin.thule.people.domain.address;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkAddressTest {
    private final TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builderAndGettersOperateOnTheSameField() {
        // Given
        WorkAddress expectedWorkAddress = testDataFactory.newRegentStreetWorkAddress();

        // When
        WorkAddress actualWorkAddress = WorkAddress.WorkAddressBuilder.aWorkAddress().
                withAddressLine1(expectedWorkAddress.getAddressLine1()).
                withAddressLine2(expectedWorkAddress.getAddressLine2()).
                withCountry(expectedWorkAddress.getCountry()).
                withCounty(expectedWorkAddress.getCounty()).
                withCreatedAt(expectedWorkAddress.getCreatedAt()).
                withId(expectedWorkAddress.getId()).
                withPostCode(expectedWorkAddress.getPostCode()).
                withState(expectedWorkAddress.getState()).
                withTown(expectedWorkAddress.getTown()).
                withUpdatedAt(expectedWorkAddress.getUpdatedAt()).
                withUpdatedBy(expectedWorkAddress.getUpdatedBy()).
                withVersion(expectedWorkAddress.getVersion()).build();

        // Then
        assertThat(actualWorkAddress.getAddressLine1()).isEqualTo(expectedWorkAddress.getAddressLine1());
        assertThat(actualWorkAddress.getAddressLine2()).isEqualTo(expectedWorkAddress.getAddressLine2());
        assertThat(actualWorkAddress.getCountry()).isEqualTo(expectedWorkAddress.getCountry());
        assertThat(actualWorkAddress.getCounty()).isEqualTo(expectedWorkAddress.getCounty());
        assertThat(actualWorkAddress.getCreatedAt()).isEqualTo(expectedWorkAddress.getCreatedAt());
        assertThat(actualWorkAddress.getId()).isEqualTo(expectedWorkAddress.getId());
        assertThat(actualWorkAddress.getPostCode()).isEqualTo(expectedWorkAddress.getPostCode());
        assertThat(actualWorkAddress.getState()).isEqualTo(expectedWorkAddress.getState());
        assertThat(actualWorkAddress.getTown()).isEqualTo(expectedWorkAddress.getTown());
        assertThat(actualWorkAddress.getUpdatedAt()).isEqualTo(expectedWorkAddress.getUpdatedAt());
        assertThat(actualWorkAddress.getUpdatedBy()).isEqualTo(expectedWorkAddress.getUpdatedBy());
        assertThat(actualWorkAddress.getVersion()).isEqualTo(expectedWorkAddress.getVersion());
    }

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        String addressLine1 = "addressLine1";
        Country country = testDataFactory.getCountries().get(Country.GBR);
        String postCode = "postCode";

        // When
        WorkAddress workAddress = new WorkAddress(addressLine1, postCode, country);

        // Then
        assertThat(workAddress.getAddressLine1()).isEqualTo(addressLine1);
        assertThat(workAddress.getCountry()).isEqualTo(country);
        assertThat(workAddress.getPostCode()).isEqualTo(postCode);
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
        WorkAddress expectedWorkAddress = testDataFactory.newRegentStreetWorkAddress();

        // When
        WorkAddress actualWorkAddress = new WorkAddress(expectedWorkAddress.getAddressLine1(), expectedWorkAddress.getPostCode(), expectedWorkAddress.getCountry());
        actualWorkAddress.setAddressLine2(expectedWorkAddress.getAddressLine2());
        actualWorkAddress.setCounty(expectedWorkAddress.getCounty());
        actualWorkAddress.setState(expectedWorkAddress.getState());
        actualWorkAddress.setTown(expectedWorkAddress.getTown());

        // Then
        assertThat(actualWorkAddress.getAddressLine1()).isEqualTo(expectedWorkAddress.getAddressLine1());
        assertThat(actualWorkAddress.getAddressLine2()).isEqualTo(expectedWorkAddress.getAddressLine2());
        assertThat(actualWorkAddress.getCountry()).isEqualTo(expectedWorkAddress.getCountry());
        assertThat(actualWorkAddress.getCounty()).isEqualTo(expectedWorkAddress.getCounty());
        assertThat(actualWorkAddress.getCreatedAt()).isEqualTo(expectedWorkAddress.getCreatedAt());
        assertThat(actualWorkAddress.getId()).isEqualTo(expectedWorkAddress.getId());
        assertThat(actualWorkAddress.getPostCode()).isEqualTo(expectedWorkAddress.getPostCode());
        assertThat(actualWorkAddress.getState()).isEqualTo(expectedWorkAddress.getState());
        assertThat(actualWorkAddress.getTown()).isEqualTo(expectedWorkAddress.getTown());
        assertThat(actualWorkAddress.getUpdatedAt()).isEqualTo(expectedWorkAddress.getUpdatedAt());
        assertThat(actualWorkAddress.getUpdatedBy()).isEqualTo(expectedWorkAddress.getUpdatedBy());
        assertThat(actualWorkAddress.getVersion()).isEqualTo(expectedWorkAddress.getVersion());
    }

    @Test
    public void toStringIsOverridden() {
        // Given
        String addressLine1 = "addressLine1";
        Country country = testDataFactory.getCountries().get(Country.GBR);
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
                withOnlyTheseFields(Address.ENTITY_ATTRIBUTE_NAME_ADDRESS_LINE_1, Address.ENTITY_ATTRIBUTE_NAME_COUNTRY, Address.ENTITY_ATTRIBUTE_NAME_POST_CODE).
                verify();
    }
}