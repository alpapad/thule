package uk.co.serin.thule.people.rest.projection;

import org.springframework.data.rest.core.config.Projection;

import uk.co.serin.thule.people.domain.entity.account.AccountEntity;

@Projection(name = "summary", types = {AccountEntity.class})
public interface AccountProjection {
    byte[] getPhoto();

    long getPosition();
}
