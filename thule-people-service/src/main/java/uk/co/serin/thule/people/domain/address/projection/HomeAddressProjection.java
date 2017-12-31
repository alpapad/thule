package uk.co.serin.thule.people.domain.address.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.address.HomeAddress;

@Projection(name = "summary", types = {HomeAddress.class})
public interface HomeAddressProjection {
    String getAddressLine1();

    String getAddressLine2();

    @Value("#{target.country.isoName}")
    String getCountry();

    String getCounty();

    String getPostCode();

    String getTown();
}
