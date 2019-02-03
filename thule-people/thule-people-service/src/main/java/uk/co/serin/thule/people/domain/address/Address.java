package uk.co.serin.thule.people.domain.address;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.country.Country;
import uk.co.serin.thule.people.domain.state.State;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = DomainModel.DATABASE_COLUMN_ADDRESS_TYPE, discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = DomainModel.DATABASE_COLUMN_ADDRESS_TYPE_VALUE_UNSPECIFIED)
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Table(name = DomainModel.ENTITY_NAME_ADDRESSES)
@ToString(callSuper = true)
public abstract class Address extends DomainModel {
    private static final int ADDRESS_LINE1_MAX_LENGTH = 30;
    private static final int ADDRESS_LINE2_MAX_LENGTH = 30;
    private static final int COUNTY_MAX_LENGTH = 30;
    private static final int POSTCODE_MAX_LENGTH = 9;
    private static final int TOWN_MAX_LENGTH = 30;

    @EqualsAndHashCode.Include
    @NotEmpty
    @Size(max = ADDRESS_LINE1_MAX_LENGTH)
    private String addressLine1;

    @Size(max = ADDRESS_LINE2_MAX_LENGTH)
    private String addressLine2;

    @EqualsAndHashCode.Include
    @JoinColumn(name = DATABASE_COLUMN_COUNTRY_ID, nullable = false, updatable = false)
    @ManyToOne(optional = false)
    @NotNull
    private Country country;

    @NotEmpty
    @Size(max = COUNTY_MAX_LENGTH)
    private String county;

    @EqualsAndHashCode.Include
    @NotEmpty
    @Size(max = POSTCODE_MAX_LENGTH)
    private String postCode;

    @JoinColumn(name = DATABASE_COLUMN_STATE_ID, nullable = false, updatable = false)
    @ManyToOne(optional = false)
    @NotNull
    private State state;

    @NotEmpty
    @Size(max = TOWN_MAX_LENGTH)
    private String town;
}
