package uk.co.serin.thule.people.domain.state;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Transient;

import uk.co.serin.thule.people.domain.DomainModel;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = DomainModel.ENTITY_NAME_STATES)
public final class State extends DomainModel {
    private static final int DESCRIPTION_MAX_LENGTH = 100;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = DATABASE_TABLE_STATE_ACTIONS,
            joinColumns = {@JoinColumn(name = DATABASE_COLUMN_STATE_ID, nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = DATABASE_COLUMN_ACTION_ID, nullable = false)})
    @OrderBy(value = ENTITY_ATTRIBUTE_NAME_DESCRIPTION)
    @Transient
    private final Set<Action> actions = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private StateCode code;

    @Column
    @NotNull
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;

    /**
     * Default constructor required when instantiating as java bean, e.g. by hibernate or jackson
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    State() {
    }

    /**
     * Business key constructor
     *
     * @param code Business key attribute
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    public State(StateCode code) {
        this.code = code;
    }

    public void addActions(Set<Action> actions) {
        this.actions.addAll(actions);
    }

    @JsonIgnore
    public Map<ActionCode, Action> getActionsByCode() {
        return getActions().stream().collect(Collectors.toMap(Action::getCode, Function.identity()));
    }

    public Set<Action> getActions() {
        return Collections.unmodifiableSet(actions);
    }

    public StateCode getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        State state = (State) o;
        return code == state.code;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "State{", "}")
                .add(super.toString())
                .add(String.format("code=%s", code))
                .add(String.format("description=%s", description))
                .toString();
    }

    public static final class StateBuilder {
        private Set<Action> actions = new HashSet<>();
        private StateCode code;
        private LocalDateTime createdAt;
        private String createdBy;
        private String description;
        private Long id;
        private LocalDateTime updatedAt;
        private String updatedBy;
        private Long version;

        private StateBuilder() {
        }

        public static StateBuilder aState() {
            return new StateBuilder();
        }

        public State build() {
            State state = new State(code);
            state.setDescription(description);
            state.addActions(actions);
            return state;
        }

        public StateBuilder withActions(Set<Action> actions) {
            this.actions = actions;
            return this;
        }

        public StateBuilder withCode(StateCode code) {
            this.code = code;
            return this;
        }

        public StateBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public StateBuilder withCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public StateBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public StateBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public StateBuilder withUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public StateBuilder withUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public StateBuilder withVersion(Long version) {
            this.version = version;
            return this;
        }
    }
}
