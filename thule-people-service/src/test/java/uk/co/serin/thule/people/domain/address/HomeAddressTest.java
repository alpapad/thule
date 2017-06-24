package uk.co.serin.thule.people.domain.address;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeAddressTest {
    private final TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builder_and_getters_operate_on_the_same_field() {
        // Given
        HomeAddress expectedHomeAddress = testDataFactory.buildOxfordStreetHomeAddress();

        // When
        HomeAddress actualHomeAddress = HomeAddress.HomeAddressBuilder.aHomeAddress().
                withAddressLine1(expectedHomeAddress.getAddressLine1()).
                withAddressLine2(expectedHomeAddress.getAddressLine2()).
                withCountry(expectedHomeAddress.getCountry()).
                withCounty(expectedHomeAddress.getCounty()).
                withCreatedAt(expectedHomeAddress.getCreatedAt()).
                withId(expectedHomeAddress.getId()).
                withPostCode(expectedHomeAddress.getPostCode()).
                withState(expectedHomeAddress.getState()).
                withTown(expectedHomeAddress.getTown()).
                withUpdatedAt(expectedHomeAddress.getUpdatedAt()).
                withUpdatedBy(expectedHomeAddress.getUpdatedBy()).
                withVersion(expectedHomeAddress.getVersion()).build();

        // Then
        assertThat(actualHomeAddress.getAddressLine1()).isEqualTo(expectedHomeAddress.getAddressLine1());
        assertThat(actualHomeAddress.getAddressLine2()).isEqualTo(expectedHomeAddress.getAddressLine2());
        assertThat(actualHomeAddress.getCountry()).isEqualTo(expectedHomeAddress.getCountry());
        assertThat(actualHomeAddress.getCounty()).isEqualTo(expectedHomeAddress.getCounty());
        assertThat(actualHomeAddress.getCreatedAt()).isEqualTo(expectedHomeAddress.getCreatedAt());
        assertThat(actualHomeAddress.getId()).isEqualTo(expectedHomeAddress.getId());
        assertThat(actualHomeAddress.getPostCode()).isEqualTo(expectedHomeAddress.getPostCode());
        assertThat(actualHomeAddress.getState()).isEqualTo(expectedHomeAddress.getState());
        assertThat(actualHomeAddress.getTown()).isEqualTo(expectedHomeAddress.getTown());
        assertThat(actualHomeAddress.getUpdatedAt()).isEqualTo(expectedHomeAddress.getUpdatedAt());
        assertThat(actualHomeAddress.getUpdatedBy()).isEqualTo(expectedHomeAddress.getUpdatedBy());
        assertThat(actualHomeAddress.getVersion()).isEqualTo(expectedHomeAddress.getVersion());
    }

    @Test
    public void business_key_constructor_creates_instance_with_correct_key() {
        // Given
        String addressLine1 = "addressLine1";
        Country country = testDataFactory.getCountries().get(Country.GBR);
        String postCode = "postCode";

        // When
        HomeAddress homeAddress = new HomeAddress(addressLine1, postCode, country);

        // Then
        assertThat(homeAddress.getAddressLine1()).isEqualTo(addressLine1);
        assertThat(homeAddress.getCountry()).isEqualTo(country);
        assertThat(homeAddress.getPostCode()).isEqualTo(postCode);
    }

    @Test
    public void default_constructor_creates_instance_successfully() {
        // Given

        // When
        HomeAddress homeAddress = new HomeAddress();

        // Then
        assertThat(homeAddress).isNotNull();
    }

    @Test
    public void getters_and_setters_operate_on_the_same_field() {
        // Given
        HomeAddress expectedHomeAddress = testDataFactory.buildOxfordStreetHomeAddress();

        // When
        HomeAddress actualHomeAddress = new HomeAddress(expectedHomeAddress.getAddressLine1(), expectedHomeAddress.getPostCode(), expectedHomeAddress.getCountry());
        actualHomeAddress.setAddressLine2(expectedHomeAddress.getAddressLine2());
        actualHomeAddress.setCounty(expectedHomeAddress.getCounty());
        actualHomeAddress.setState(expectedHomeAddress.getState());
        actualHomeAddress.setTown(expectedHomeAddress.getTown());

        // Then
        assertThat(actualHomeAddress.getAddressLine1()).isEqualTo(expectedHomeAddress.getAddressLine1());
        assertThat(actualHomeAddress.getAddressLine2()).isEqualTo(expectedHomeAddress.getAddressLine2());
        assertThat(actualHomeAddress.getCountry()).isEqualTo(expectedHomeAddress.getCountry());
        assertThat(actualHomeAddress.getCounty()).isEqualTo(expectedHomeAddress.getCounty());
        assertThat(actualHomeAddress.getCreatedAt()).isEqualTo(expectedHomeAddress.getCreatedAt());
        assertThat(actualHomeAddress.getId()).isEqualTo(expectedHomeAddress.getId());
        assertThat(actualHomeAddress.getPostCode()).isEqualTo(expectedHomeAddress.getPostCode());
        assertThat(actualHomeAddress.getState()).isEqualTo(expectedHomeAddress.getState());
        assertThat(actualHomeAddress.getTown()).isEqualTo(expectedHomeAddress.getTown());
        assertThat(actualHomeAddress.getUpdatedAt()).isEqualTo(expectedHomeAddress.getUpdatedAt());
        assertThat(actualHomeAddress.getUpdatedBy()).isEqualTo(expectedHomeAddress.getUpdatedBy());
        assertThat(actualHomeAddress.getVersion()).isEqualTo(expectedHomeAddress.getVersion());
    }

    @Test
    public void toString_is_overridden() {
        // Given
        String addressLine1 = "addressLine1";
        Country country = testDataFactory.getCountries().get(Country.GBR);
        String postCode = "postCode";

        // When
        HomeAddress homeAddress = new HomeAddress(addressLine1, postCode, country);

        // Then
        assertThat(homeAddress.toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_ADDRESS_LINE_1);
    }

    @Test
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(HomeAddress.class).
                withPrefabValues(Action.class, new Action(ActionCode.ADDRESS_DISABLE), new Action(ActionCode.ADDRESS_DISCARD)).
                withOnlyTheseFields(Address.ENTITY_ATTRIBUTE_NAME_ADDRESS_LINE_1, Address.ENTITY_ATTRIBUTE_NAME_COUNTRY, Address.ENTITY_ATTRIBUTE_NAME_POST_CODE).
                verify();
    }
}