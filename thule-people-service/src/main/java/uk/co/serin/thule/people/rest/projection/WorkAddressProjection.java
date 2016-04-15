package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.State;

@Projection(name = "summary", types = {WorkAddress.class})
public interface WorkAddressProjection {
    String getAddressLine1();

    String getAddressLine2();

    Country getCountry();

    String getCounty();

    String getPostCode();

    State getState();

    String getTown();
}
