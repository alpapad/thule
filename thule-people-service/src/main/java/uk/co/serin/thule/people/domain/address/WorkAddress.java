package uk.co.serin.thule.people.domain.address;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity()
@DiscriminatorValue(DomainModel.DATABASE_COLUMN_ADDRESS_TYPE_VALUE_WORK)
public final class WorkAddress extends Address {
    private static final long serialVersionUID = -2426501155069581033L;

    /**
     * Default constructor required by Hibernate
     */
    WorkAddress() {
    }

    public WorkAddress(WorkAddress workAddress) {
        super(workAddress);
    }

    public WorkAddress(String addressLine1, String postCode, Country country) {
        super(addressLine1, postCode, country);
    }

}
