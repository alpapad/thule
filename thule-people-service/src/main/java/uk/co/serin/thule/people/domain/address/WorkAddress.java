package uk.co.serin.thule.people.domain.address;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.State;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity()
@DiscriminatorValue(DomainModel.DATABASE_COLUMN_ADDRESS_TYPE_VALUE_WORK)
public final class WorkAddress extends Address {
    /**
     * Default constructor required when instantiating as java bean, e.g. by hibernate or jackson
     */
    protected WorkAddress() {
    }

    /**
     * Business key constructor
     *
     * @param addressLine1 Business key attribute
     * @param postCode     Business key attribute
     * @param country      Business key attribute
     */
    public WorkAddress(String addressLine1, String postCode, Country country) {
        super(addressLine1, postCode, country);
    }

    public static final class WorkAddressBuilder {
        private String addressLine1;
        private String addressLine2;
        private Country country;
        private String county;
        private LocalDateTime createdAt;
        private Long id;
        private String postCode;
        private State state;
        private String town;
        private LocalDateTime updatedAt;

        private String updatedBy;
        private Long version;

        private WorkAddressBuilder() {
        }

        public static WorkAddressBuilder aWorkAddress() {
            return new WorkAddressBuilder();
        }

        public WorkAddress build() {
            WorkAddress workAddress = new WorkAddress(addressLine1, postCode, country);
            workAddress.setAddressLine2(addressLine2);
            workAddress.setCounty(county);
            workAddress.setState(state);
            workAddress.setTown(town);
            workAddress.setCreatedAt(createdAt);
            workAddress.setId(id);
            workAddress.setUpdatedAt(updatedAt);
            workAddress.setUpdatedBy(updatedBy);
            workAddress.setVersion(version);
            return workAddress;
        }

        public WorkAddressBuilder withAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public WorkAddressBuilder withAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public WorkAddressBuilder withCountry(Country country) {
            this.country = country;
            return this;
        }

        public WorkAddressBuilder withCounty(String county) {
            this.county = county;
            return this;
        }

        public WorkAddressBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public WorkAddressBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public WorkAddressBuilder withPostCode(String postCode) {
            this.postCode = postCode;
            return this;
        }

        public WorkAddressBuilder withState(State state) {
            this.state = state;
            return this;
        }

        public WorkAddressBuilder withTown(String town) {
            this.town = town;
            return this;
        }

        public WorkAddressBuilder withUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public WorkAddressBuilder withUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public WorkAddressBuilder withVersion(Long version) {
            this.version = version;
            return this;
        }
    }
}
