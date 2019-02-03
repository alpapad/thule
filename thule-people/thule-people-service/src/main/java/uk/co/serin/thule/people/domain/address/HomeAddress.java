package uk.co.serin.thule.people.domain.address;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.State;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@DiscriminatorValue(DomainModel.DATABASE_COLUMN_ADDRESS_TYPE_VALUE_HOME)
@Entity()
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class HomeAddress extends Address {
    @Builder
    public HomeAddress(String addressLine1, String addressLine2, Country country, String county, String postCode, State state, String town) {
        super(addressLine1, addressLine2, country, county, postCode, state, town);
    }
}
