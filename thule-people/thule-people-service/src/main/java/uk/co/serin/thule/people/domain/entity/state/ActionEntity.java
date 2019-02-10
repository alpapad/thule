package uk.co.serin.thule.people.domain.entity.state;

import com.fasterxml.jackson.annotation.JsonIgnore;

import uk.co.serin.thule.people.domain.entity.AuditEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = AuditEntity.ENTITY_NAME_ACTIONS)
@ToString(callSuper = true)
public class ActionEntity extends AuditEntity {
    private static final int DESCRIPTION_MAX_LENGTH = 100;

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    @NotNull
    private ActionCode code;

    @NotEmpty
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = ENTITY_ATTRIBUTE_NAME_NEXT_STATE_ID, updatable = false)
    @JsonIgnore
    @ToString.Exclude
    private StateEntity nextState;
}
