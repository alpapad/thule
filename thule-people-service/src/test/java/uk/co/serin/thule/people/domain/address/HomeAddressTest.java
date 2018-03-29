package uk.co.serin.thule.people.domain.address;

import org.junit.Test;

import uk.co.serin.thule.people.datafactory.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

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
    public void pojo_methods_are_well_implemented() {
        assertPojoMethodsFor(HomeAddress.class).areWellImplemented();
    }
}