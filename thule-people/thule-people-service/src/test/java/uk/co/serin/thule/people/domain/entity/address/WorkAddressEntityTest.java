package uk.co.serin.thule.people.domain.entity.address;

import org.junit.Test;

import uk.co.serin.thule.people.datafactory.MockReferenceDataFactory;
import uk.co.serin.thule.people.datafactory.ReferenceDataFactory;
import uk.co.serin.thule.people.domain.entity.country.CountryEntity;
import uk.co.serin.thule.people.domain.model.state.StateCode;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkAddressEntityTest {
    private static final String GREATER_LONDON = "Greater London";
    private static final String LONDON = "London";
    private ReferenceDataFactory referenceDataFactory = new MockReferenceDataFactory();

    @Test
    public void when_constructing_then_a_work_address_is_instantiated() {
        // When
        var workAddress =
                WorkAddressEntity.builder().addressLine1("Regent Street").addressLine2("Green").country(referenceDataFactory.getCountries().get(CountryEntity.GBR))
                                 .county(GREATER_LONDON).postCode("EC4").state(referenceDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).town(LONDON)
                                 .build();

        // Then
        assertThat(workAddress).isNotNull();
    }
}