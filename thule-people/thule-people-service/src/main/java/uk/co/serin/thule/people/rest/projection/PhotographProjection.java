package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.entity.person.PhotographEntity;

@Projection(name = "summary", types = {PhotographEntity.class})
public interface PhotographProjection {
    byte[] getPhoto();

    long getPosition();
}
