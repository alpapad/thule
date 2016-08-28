package uk.co.serin.thule.people.domain.role;

import org.springframework.beans.BeanUtils;

import uk.co.serin.thule.people.domain.DomainModel;

import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = DomainModel.ENTITY_NAME_ROLES)
public final class Role extends DomainModel {
    private static final int DESCRIPTION_MAX_LENGTH = 30;
    private static final long serialVersionUID = 3554047495140157877L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    @NotNull
    private RoleCode code;

    @Column(length = DESCRIPTION_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;

    /**
     * Default constructor required by Hibernate
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    Role() {
    }

    /**
     * Copy object constructor
     *
     * @param role Object to be copied
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Role(Role role) {
        // Copy business key
        this.code = role.code;
        // Copy mutable properties, i.e. those with a setter
        BeanUtils.copyProperties(role, this);
        // Copy immutable properties, i.e. those without a setter
        setCreatedAt(role.getCreatedAt());
        setUpdatedAt(role.getUpdatedAt());
        setUpdatedBy(role.getUpdatedBy());
    }

    /**
     * Business key constructor
     *
     * @param code Business key attribute
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Role(RoleCode code) {
        this.code = code;
    }

    public RoleCode getCode() {
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
        Role role = (Role) o;
        return code == role.code;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Role{", "}")
                .add(String.format("code=%s", code))
                .add(String.format("description=%s", description))
                .toString();
    }
}
