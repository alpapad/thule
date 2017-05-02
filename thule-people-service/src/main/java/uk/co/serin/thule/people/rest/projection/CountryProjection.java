package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.country.Country;

@Projection(name = "summary", types = {Country.class})
public interface CountryProjection {
    String getIsoCodeThreeDigit();

    String getIsoCodeTwoDigit();

    String getIsoName();

    String getIsoNumber();
}
