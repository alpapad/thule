package uk.co.serin.thule.people.domain.entity.state;

import org.springframework.data.annotation.Transient;

import uk.co.serin.thule.people.domain.entity.AuditEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
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

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Table(name = AuditEntity.ENTITY_NAME_STATES)
@ToString(callSuper = true)
public class StateEntity extends AuditEntity {
    private static final int DESCRIPTION_MAX_LENGTH = 100;

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = DATABASE_TABLE_STATE_ACTIONS,
            joinColumns = {@JoinColumn(name = DATABASE_COLUMN_STATE_ID, nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = DATABASE_COLUMN_ACTION_ID, nullable = false)})
    @OrderBy(value = ENTITY_ATTRIBUTE_NAME_DESCRIPTION)
    @ToString.Exclude
    @Transient
    private Set<ActionEntity> actions = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    @NotNull
    private StateCode code;

    @NotEmpty
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;
}
