package uk.co.serin.thule.people.domain.entity.country;

import uk.co.serin.thule.people.domain.entity.AuditEntity;

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
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Table(name = "countries")
@ToString(callSuper = true)
public class CountryEntity extends AuditEntity {
    public static final String GBR = "GBR";
    private static final int ISO_CODE_THREE_CHARACTER_LENGTH = 3;
    private static final int ISO_CODE_TWO_CHARACTER_LENGTH = 2;
    private static final int ISO_NAME_MAX_LENGTH = 100;
    private static final int ISO_NUMBER_LENGTH = 3;

    @EqualsAndHashCode.Include
    @NotEmpty
    @Size(min = ISO_CODE_THREE_CHARACTER_LENGTH, max = ISO_CODE_THREE_CHARACTER_LENGTH)
    private String isoCodeThreeCharacters;

    @NotEmpty
    @Size(min = ISO_CODE_TWO_CHARACTER_LENGTH, max = ISO_CODE_TWO_CHARACTER_LENGTH)
    private String isoCodeTwoCharacters;

    @NotEmpty
    @Size(max = ISO_NAME_MAX_LENGTH)
    private String isoName;

    @NotEmpty
    @Size(max = ISO_NUMBER_LENGTH)
    private String isoNumber;
}
