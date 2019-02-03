package uk.co.serin.thule.people.domain.address;

import org.junit.Test;

import uk.co.serin.thule.people.datafactory.TestDataFactory;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.StateCode;

public class WorkAddressTest {
    private static final String GREATER_LONDON = "Greater London";
    private static final String LONDON = "London";
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void when_constructing_then_a_work_address_is_instantiated() {
        WorkAddress.builder().addressLine1("Regent Street").addressLine2("Green").country(testDataFactory.getCountries().get(Country.GBR))
                   .county(GREATER_LONDON).postCode("EC4").state(testDataFactory.getStates().get(StateCode.ADDRESS_ENABLED)).town(LONDON).build();
    }
}