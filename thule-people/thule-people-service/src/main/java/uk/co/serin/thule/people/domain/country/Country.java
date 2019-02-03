package uk.co.serin.thule.people.domain.country;

import uk.co.serin.thule.people.domain.DomainModel;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Table(name = DomainModel.ENTITY_NAME_COUNTRIES)
@ToString(callSuper = true)
public class Country extends DomainModel {
    public static final String GBR = "GBR";
    private static final int ISO_CODE_THREE_DIGIT_MAX_LENGTH = 3;
    private static final int ISO_CODE_TWO_DIGIT_MAX_LENGTH = 2;
    private static final int ISO_NAME_MAX_LENGTH = 100;

    @EqualsAndHashCode.Include
    @NotEmpty
    @Size(max = ISO_CODE_THREE_DIGIT_MAX_LENGTH)
    private String isoCodeThreeDigit;

    @NotEmpty
    @Size(max = ISO_CODE_TWO_DIGIT_MAX_LENGTH)
    private String isoCodeTwoDigit;

    @NotEmpty
    @Size(max = ISO_NAME_MAX_LENGTH)
    private String isoName;

    @NotEmpty
    @Size(max = ISO_CODE_THREE_DIGIT_MAX_LENGTH)
    private String isoNumber;
}
