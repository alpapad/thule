package uk.co.serin.thule.people.domain.country;

import org.springframework.beans.BeanUtils;

import uk.co.serin.thule.people.domain.DomainModel;

import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = DomainModel.ENTITY_NAME_COUNTRIES)
public final class Country extends DomainModel {
    public static final String GBR = "GBR";
    private static final int ISO_CODE_THREE_DIGIT_MAX_LENGTH = 3;
    private static final int ISO_CODE_TWO_DIGIT_MAX_LENGTH = 2;
    private static final int ISO_NAME_MAX_LENGTH = 100;
    private static final long serialVersionUID = 7038192379056508570L;

    @Column(length = ISO_CODE_THREE_DIGIT_MAX_LENGTH, nullable = false, unique = true)
    @NotNull
    @Size(max = ISO_CODE_THREE_DIGIT_MAX_LENGTH)
    private String isoCodeThreeDigit;

    @Column(length = ISO_CODE_TWO_DIGIT_MAX_LENGTH, nullable = false, unique = true)
    @NotNull
    @Size(max = ISO_CODE_TWO_DIGIT_MAX_LENGTH)
    private String isoCodeTwoDigit;

    @Column(length = ISO_NAME_MAX_LENGTH, nullable = false, unique = true)
    @NotNull
    @Size(max = ISO_NAME_MAX_LENGTH)
    private String isoName;

    @Column(length = ISO_CODE_THREE_DIGIT_MAX_LENGTH, nullable = false, unique = true)
    @NotNull
    @Size(max = ISO_CODE_THREE_DIGIT_MAX_LENGTH)
    private String isoNumber;

    /**
     * Default constructor required by Hibernate
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    Country() {
    }

    /**
     * Copy object constructor
     * @param country Object to be copied
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Country(Country country) {
        // Copy business key
        this.isoCodeThreeDigit = country.isoCodeThreeDigit;
        // Copy mutable properties, i.e. those with a setter
        BeanUtils.copyProperties(country, this);
        // Copy immutable properties, i.e. those without a setter
        getAudit().setCreatedAt(country.getAudit().getCreatedAt());
        getAudit().setUpdatedAt(country.getAudit().getUpdatedAt());
        getAudit().setUpdatedBy(country.getAudit().getUpdatedBy());
    }

    /**
     * Business key constructor
     * @param isoCodeThreeDigit Business key attribute
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Country(String isoCodeThreeDigit) {
        this.isoCodeThreeDigit = isoCodeThreeDigit;
    }

    public String getIsoCodeThreeDigit() {
        return isoCodeThreeDigit;
    }
    
    public String getIsoCodeTwoDigit() {
        return isoCodeTwoDigit;
    }

    public void setIsoCodeTwoDigit(String isoCodeTwoDigit) {
        this.isoCodeTwoDigit = isoCodeTwoDigit;
    }

    public String getIsoName() {
        return isoName;
    }

    public void setIsoName(String isoName) {
        this.isoName = isoName;
    }

    public String getIsoNumber() {
        return isoNumber;
    }

    public void setIsoNumber(String isoNumber) {
        this.isoNumber = isoNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isoCodeThreeDigit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Country country = (Country) o;
        return Objects.equals(isoCodeThreeDigit, country.isoCodeThreeDigit);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Country{", "}")
                .add(String.format("isoCodeThreeDigit=%s", isoCodeThreeDigit))
                .add(String.format("isoCodeTwoDigit=%s", isoCodeTwoDigit))
                .add(String.format("isoName=%s", isoName))
                .add(String.format("isoNumber=%s", isoNumber))
                .toString();
    }
}
