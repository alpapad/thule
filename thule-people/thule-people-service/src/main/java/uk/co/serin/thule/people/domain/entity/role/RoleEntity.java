package uk.co.serin.thule.people.domain.entity.role;

import uk.co.serin.thule.people.domain.entity.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Table(name = AuditEntity.ENTITY_NAME_ROLES)
@ToString(callSuper = true)
public class RoleEntity extends AuditEntity {
    private static final int DESCRIPTION_MAX_LENGTH = 30;

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    @NotNull
    private RoleCode code;

    @NotEmpty
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;
}

