package uk.co.serin.thule.people.domain.state;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.BeanUtils;

import uk.co.serin.thule.people.domain.DomainModel;

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
    private static final long serialVersionUID = -2795098920091915554L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    @NotNull
    private ActionCode code;

    @Column(length = DESCRIPTION_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = ENTITY_ATTRIBUTE_NAME_NEXT_STATE_ID, updatable = false)
    @JsonIgnore
    private State nextState;

    /**
     * Default constructor required by Hibernate
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    Action() {
    }

    /**
     * Copy object constructor
     *
     * @param action Object to be copied
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Action(Action action) {
        // Copy business key
        this.code = action.code;
        // Copy mutable properties, i.e. those with a setter
        BeanUtils.copyProperties(action, this);
        // Copy immutable properties, i.e. those without a setter
        setCreatedAt(action.getCreatedAt());
        setUpdatedAt(action.getUpdatedAt());
        setUpdatedBy(action.getUpdatedBy());
    }

    /**
     * Business key constructor
     *
     * @param code Business key attribute
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
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
                .add(String.format("code=%s", code))
                .add(String.format("description=%s", description))
                .add(String.format("nextState=%s", nextState))
                .toString();
    }
}
