package uk.co.serin.thule.people.domain.country;

import uk.co.serin.thule.people.domain.DomainModel;

import java.time.LocalDateTime;
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
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    Country() {
    }

    /**
     * Business key constructor
     *
     * @param isoCodeThreeDigit Business key attribute
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
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
                .add(super.toString())
                .add(String.format("isoCodeThreeDigit=%s", isoCodeThreeDigit))
                .add(String.format("isoCodeTwoDigit=%s", isoCodeTwoDigit))
                .add(String.format("isoName=%s", isoName))
                .add(String.format("isoNumber=%s", isoNumber))
                .toString();
    }

    public static final class CountryBuilder {
        private LocalDateTime createdAt;
        private Long id;
        private String isoCodeThreeDigit;
        private String isoCodeTwoDigit;
        private String isoName;
        private String isoNumber;
        private LocalDateTime updatedAt;

        private String updatedBy;
        private Long version;

        private CountryBuilder() {
        }

        public static CountryBuilder aCountry() {
            return new CountryBuilder();
        }

        public Country build() {
            Country country = new Country(isoCodeThreeDigit);
            country.setCreatedAt(createdAt);
            country.setId(id);
            country.setIsoCodeTwoDigit(isoCodeTwoDigit);
            country.setIsoName(isoName);
            country.setIsoNumber(isoNumber);
            country.setUpdatedAt(updatedAt);
            country.setUpdatedBy(updatedBy);
            country.setVersion(version);
            return country;
        }

        public CountryBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CountryBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CountryBuilder withIsoCodeThreeDigit(String isoCodeThreeDigit) {
            this.isoCodeThreeDigit = isoCodeThreeDigit;
            return this;
        }

        public CountryBuilder withIsoCodeTwoDigit(String isoCodeTwoDigit) {
            this.isoCodeTwoDigit = isoCodeTwoDigit;
            return this;
        }

        public CountryBuilder withIsoName(String isoName) {
            this.isoName = isoName;
            return this;
        }

        public CountryBuilder withIsoNumber(String isoNumber) {
            this.isoNumber = isoNumber;
            return this;
        }

        public CountryBuilder withUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public CountryBuilder withUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public CountryBuilder withVersion(Long version) {
            this.version = version;
            return this;
        }
    }
}
