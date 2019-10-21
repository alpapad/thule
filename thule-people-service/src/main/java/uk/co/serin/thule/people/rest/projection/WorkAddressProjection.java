package uk.co.serin.thule.people.rest.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.entity.address.WorkAddressEntity;

@Projection(name = "summary", types = {WorkAddressEntity.class})
public interface WorkAddressProjection {
    String getAddressLine1();

    String getAddressLine2();

    @Value("#{target.country.isoName}")
    String getCountry();

    String getCounty();

    String getPostCode();

    String getTown();
}
