package uk.co.serin.thule.people.domain.address.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.address.WorkAddress;

@Projection(name = "summary", types = {WorkAddress.class})
public interface WorkAddressProjection {
    String getAddressLine1();

    String getAddressLine2();

    @Value("#{target.country.isoName}")
    String getCountry();

    String getCounty();

    String getPostCode();

    String getTown();
}
