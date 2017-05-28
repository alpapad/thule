package uk.co.serin.thule.people.domain.address;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.State;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(DomainModel.DATABASE_COLUMN_ADDRESS_TYPE_VALUE_HOME)
public final class HomeAddress extends Address<HomeAddress> {
    /**
     * Default constructor required by Hibernate
     */
    HomeAddress() {
    }

    /**
     * Business key constructor
     *
     * @param addressLine1 Business key attribute
     * @param postCode     Business key attribute
     * @param country      Business key attribute
     */
    public HomeAddress(String addressLine1, String postCode, Country country) {
        super(addressLine1, postCode, country);
    }

    public static final class HomeAddressBuilder {
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

        private HomeAddressBuilder() {
        }

        public static HomeAddressBuilder aHomeAddress() {
            return new HomeAddressBuilder();
        }

        public HomeAddress build() {
            HomeAddress homeAddress = new HomeAddress(addressLine1, postCode, country);
            homeAddress.setAddressLine2(addressLine2);
            homeAddress.setCounty(county);
            homeAddress.setState(state);
            homeAddress.setTown(town);
            homeAddress.setCreatedAt(createdAt);
            homeAddress.setId(id);
            homeAddress.setUpdatedAt(updatedAt);
            homeAddress.setUpdatedBy(updatedBy);
            homeAddress.setVersion(version);
            return homeAddress;
        }

        public HomeAddressBuilder withAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public HomeAddressBuilder withAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public HomeAddressBuilder withCountry(Country country) {
            this.country = country;
            return this;
        }

        public HomeAddressBuilder withCounty(String county) {
            this.county = county;
            return this;
        }

        public HomeAddressBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public HomeAddressBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public HomeAddressBuilder withPostCode(String postCode) {
            this.postCode = postCode;
            return this;
        }

        public HomeAddressBuilder withState(State state) {
            this.state = state;
            return this;
        }

        public HomeAddressBuilder withTown(String town) {
            this.town = town;
            return this;
        }

        public HomeAddressBuilder withUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public HomeAddressBuilder withUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public HomeAddressBuilder withVersion(Long version) {
            this.version = version;
            return this;
        }
    }
}
