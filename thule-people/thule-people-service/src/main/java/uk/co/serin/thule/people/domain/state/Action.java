package uk.co.serin.thule.people.domain.state;

import com.fasterxml.jackson.annotation.JsonIgnore;

import uk.co.serin.thule.people.domain.DomainModel;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = DomainModel.ENTITY_NAME_ACTIONS)
public final class Action extends DomainModel {
    private static final int DESCRIPTION_MAX_LENGTH = 100;

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private ActionCode code;

    @Column
    @NotNull
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = ENTITY_ATTRIBUTE_NAME_NEXT_STATE_ID, updatable = false)
    @JsonIgnore
    private State nextState;

    /**
     * Default constructor required when instantiating as java bean, e.g. by hibernate or jackson
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    Action() {
    }

    /**
     * Business key constructor
     *
     * @param code Business key attribute
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Action(ActionCode code) {
        this.code = code;
    }

    public ActionCode getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getNextState() {
        return nextState;
    }

    public void setNextState(State nextState) {
        this.nextState = nextState;
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
        Action action = (Action) o;
        return code == action.code;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Action{", "}")
                .add(super.toString())
                .add(String.format("code=%s", code))
                .add(String.format("description=%s", description))
                .add(String.format("nextState=%s", nextState))
                .toString();
    }

    public static final class ActionBuilder {
        private ActionCode code;
        private LocalDateTime createdAt;
        private String createdBy;
        private String description;
        private Long id;
        private State nextState;
        private LocalDateTime updatedAt;
        private String updatedBy;
        private Long version;

        private ActionBuilder() {
        }

        public static ActionBuilder anAction() {
            return new ActionBuilder();
        }

        public Action build() {
            Action action = new Action(code);
            action.setDescription(description);
            action.setNextState(nextState);
            return action;
        }

        public ActionBuilder withCode(ActionCode code) {
            this.code = code;
            return this;
        }

        public ActionBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ActionBuilder withCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public ActionBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ActionBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ActionBuilder withNextState(State nextState) {
            this.nextState = nextState;
            return this;
        }

        public ActionBuilder withUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ActionBuilder withUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public ActionBuilder withVersion(Long version) {
            this.version = version;
            return this;
        }
    }
}
