package uk.co.serin.thule.people.domain.person.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.person.Photograph;

@Projection(name = "summary", types = {Photograph.class})
public interface PhotographProjection {
    byte[] getPhoto();

    long getPosition();
}
