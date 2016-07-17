package uk.co.serin.thule.people.domain.address;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(DomainModel.DATABASE_COLUMN_ADDRESS_TYPE_VALUE_HOME)
public final class HomeAddress extends Address {
    private static final long serialVersionUID = 9196176268106577091L;

    /**
     * Default constructor required by Hibernate
     */
    HomeAddress() {
    }

    /**
     * Copy object constructor
     * @param homeAddress Object to be copied
     */
    public HomeAddress(HomeAddress homeAddress) {
        super(homeAddress);
    }

    /**
     * Business key constructor
     * @param addressLine1 Business key attribute
     * @param postCode Business key attribute
     * @param country Business key attribute
     */
    public HomeAddress(String addressLine1, String postCode, Country country) {
        super(addressLine1, postCode, country);
    }
}
