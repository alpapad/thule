package uk.co.serin.thule.people.domain.entity.state;

import org.springframework.data.annotation.Transient;

import uk.co.serin.thule.people.domain.entity.AuditEntity;
import uk.co.serin.thule.people.domain.model.state.StateCode;

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
@Table(name = "states")
@ToString(callSuper = true)
public class StateEntity extends AuditEntity {
    private static final int DESCRIPTION_MAX_LENGTH = 100;

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "state_actions",
            joinColumns = {@JoinColumn(name = "state_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "action_id", nullable = false)})
    @OrderBy("description")
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
