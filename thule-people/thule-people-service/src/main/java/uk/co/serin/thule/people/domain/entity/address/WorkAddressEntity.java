package uk.co.serin.thule.people.domain.entity.address;

import uk.co.serin.thule.people.domain.entity.AuditEntity;
import uk.co.serin.thule.people.domain.entity.country.CountryEntity;
import uk.co.serin.thule.people.domain.entity.state.StateEntity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@DiscriminatorValue(AuditEntity.DATABASE_COLUMN_ADDRESS_TYPE_VALUE_WORK)
@Entity()
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class WorkAddressEntity extends AddressEntity {
    @Builder
    public WorkAddressEntity(String addressLine1, String addressLine2, CountryEntity country, String county, String postCode, StateEntity state, String town) {
        super(addressLine1, addressLine2, country, county, postCode, state, town);
    }
}
