package uk.co.serin.thule.people.domain.address;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class WorkAddressTest {
    private final TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builder_and_getters_operate_on_the_same_field() {
        // Given
        WorkAddress expectedWorkAddress = testDataFactory.buildRegentStreetWorkAddress();

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
    public void pojo_methods_are_well_implemented() {
        assertPojoMethodsFor(WorkAddress.class).areWellImplemented();
    }
}