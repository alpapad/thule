package uk.co.serin.thule.people.domain.address;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.BeanUtils;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.State;

import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = DomainModel.ENTITY_NAME_ADDRESSES)
@DiscriminatorColumn(name = DomainModel.DATABASE_COLUMN_ADDRESS_TYPE, discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = DomainModel.DATABASE_COLUMN_ADDRESS_TYPE_VALUE_UNSPECIFIED)
public abstract class Address<T extends Address> extends DomainModel {
    private static final int ADDRESS_LINE1_MAX_LENGTH = 30;
    private static final int ADDRESS_LINE2_MAX_LENGTH = 30;
    private static final int COUNTY_MAX_LENGTH = 30;
    private static final int POSTCODE_MAX_LENGTH = 9;
    private static final int TOWN_MAX_LENGTH = 30;
    private static final long serialVersionUID = 6334941299747911026L;

    @Column(length = ADDRESS_LINE1_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = ADDRESS_LINE1_MAX_LENGTH)
    private String addressLine1;

    @Column(length = ADDRESS_LINE2_MAX_LENGTH)
    @Size(max = ADDRESS_LINE2_MAX_LENGTH)
    private String addressLine2;

    @ManyToOne(optional = false)
    @JoinColumn(name = DATABASE_COLUMN_COUNTRY_ID, nullable = false, updatable = false)
    @NotNull
    @JsonIgnore
    private Country country;

    @Column(length = COUNTY_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = COUNTY_MAX_LENGTH)
    private String county;

    @Column(length = POSTCODE_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = POSTCODE_MAX_LENGTH)
    private String postCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = DATABASE_COLUMN_STATE_ID, nullable = false, updatable = false)
    @NotNull
    private State state;

    @Column(length = TOWN_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = TOWN_MAX_LENGTH)
    private String town;

    /**
     * Default constructor required by Hibernate
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    Address() {
    }

    /**
     * Copy object constructor
     *
     * @param address Object to be copied
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Address(Address address) {
        // Copy mutable inherited properties
        super(address);
        // Copy business key
        this.addressLine1 = address.addressLine1;
        this.country = new Country(address.getCountry());
        this.postCode = address.postCode;
        // Copy mutable properties
        BeanUtils.copyProperties(address, this);
    }

    /**
     * Business key constructor
     *
     * @param addressLine1 Business key attribute
     * @param postCode     Business key attribute
     * @param country      Business key attribute
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Address(String addressLine1, String postCode, Country country) {
        this.addressLine1 = addressLine1;
        this.postCode = postCode;
        this.country = country;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    @SuppressWarnings("unchecked")
    public T setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return (T) this;
    }

    public Country getCountry() {
        return country;
    }

    public String getCounty() {
        return county;
    }

    @SuppressWarnings("unchecked")
    public T setCounty(String county) {
        this.county = county;
        return (T) this;
    }

    public String getPostCode() {
        return postCode;
    }

    public State getState() {
        return state;
    }

    @SuppressWarnings("unchecked")
    public T setState(State state) {
        this.state = state;
        return (T) this;
    }

    public String getTown() {
        return town;
    }

    @SuppressWarnings("unchecked")
    public T setTown(String town) {
        this.town = town;
        return (T) this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressLine1, country, postCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Address)) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(addressLine1, address.addressLine1) &&
                Objects.equals(country, address.country) &&
                Objects.equals(postCode, address.postCode);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Address{", "}")
                .add(super.toString())
                .add(String.format("addressLine1=%s", addressLine1))
                .add(String.format("addressLine2=%s", addressLine2))
                .add(String.format("country=%s", country))
                .add(String.format("county=%s", county))
                .add(String.format("postCode=%s", postCode))
                .add(String.format("state=%s", state))
                .add(String.format("town=%s", town))
                .toString();
    }
}
