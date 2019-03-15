package uk.co.serin.thule.people.domain.entity.address;

import uk.co.serin.thule.people.domain.entity.AuditEntity;
import uk.co.serin.thule.people.domain.entity.country.CountryEntity;
import uk.co.serin.thule.people.domain.entity.state.StateEntity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
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
@DiscriminatorColumn(name = "address_type", discriminatorType = DiscriminatorType.STRING)
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Table(name = "addresses")
@ToString(callSuper = true)
public abstract class AddressEntity extends AuditEntity {
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
    @JoinColumn(name = "country_id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    @NotNull
    private CountryEntity country;

    @NotEmpty
    @Size(max = COUNTY_MAX_LENGTH)
    private String county;

    @EqualsAndHashCode.Include
    @NotEmpty
    @Size(max = POSTCODE_MAX_LENGTH)
    private String postCode;

    @JoinColumn(name = "state_id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    @NotNull
    private StateEntity stateEntity;

    @NotEmpty
    @Size(max = TOWN_MAX_LENGTH)
    private String town;
}
