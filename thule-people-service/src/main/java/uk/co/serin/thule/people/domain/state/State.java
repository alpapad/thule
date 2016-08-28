package uk.co.serin.thule.people.domain.state;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Transient;

import uk.co.serin.thule.people.domain.DomainModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static final long serialVersionUID = -6469427764755399203L;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = DATABASE_TABLE_STATE_ACTIONS,
            joinColumns = {@JoinColumn(name = DATABASE_COLUMN_STATE_ID, nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = DATABASE_COLUMN_ACTION_ID, nullable = false)})
    @OrderBy(value = ENTITY_ATTRIBUTE_NAME_DESCRIPTION)
    @Transient
    @JsonIgnore
    private final Set<Action> actions = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    @NotNull
    private StateCode code;

    @Column(length = DESCRIPTION_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;

    /**
     * Default constructor required by Hibernate
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    State() {
    }

    /**
     * Copy object constructor
     *
     * @param state Object to be copied
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public State(State state) {
        // Copy business key
        this.code = state.code;
        // Copy mutable properties, i.e. those with a setter
        BeanUtils.copyProperties(state, this);
        addActions(state.getActions().stream().map(Action::new));
        // Copy immutable properties, i.e. those without a setter
        setCreatedAt(state.getCreatedAt());
        setUpdatedAt(state.getUpdatedAt());
        setUpdatedBy(state.getUpdatedBy());
    }

    public void addActions(Stream<Action> actions) {
        this.actions.addAll(actions.collect(Collectors.toList()));
    }

    public Set<Action> getActions() {
        return Collections.unmodifiableSet(actions);
    }

    /**
     * Business key constructor
     *
     * @param code Business key attribute
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public State(StateCode code) {
        this.code = code;
    }

    @JsonIgnore
    public Map<ActionCode, Action> getActionsByCode() {
        return getActions().stream().collect(Collectors.toMap(Action::getCode, Function.identity()));
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
                .add(String.format("code=%s", code))
                .add(String.format("description=%s", description))
                .toString();
    }
}
