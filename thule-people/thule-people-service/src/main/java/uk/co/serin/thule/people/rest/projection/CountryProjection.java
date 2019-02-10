package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.entity.country.CountryEntity;

@Projection(name = "summary", types = {CountryEntity.class})
public interface CountryProjection {
    String getIsoCodeThreeCharacters();

    String getIsoCodeTwoCharacters();

    String getIsoName();

    String getIsoNumber();
}
