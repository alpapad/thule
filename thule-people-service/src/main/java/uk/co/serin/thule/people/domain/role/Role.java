package uk.co.serin.thule.people.domain.role;

import uk.co.serin.thule.people.domain.DomainModel;

import java.time.LocalDateTime;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    @NotNull
    private RoleCode code;

    @Column(length = DESCRIPTION_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;

    /**
     * Default constructor required when instantiating as java bean, e.g. by hibernate or jackson
     */
    protected Role() {
    }

    /**
     * Business key constructor
     *
     * @param code Business key attribute
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
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
                .add(super.toString())
                .add(String.format("code=%s", code))
                .add(String.format("description=%s", description))
                .toString();
    }

    public static final class RoleBuilder {
        private RoleCode code;
        private LocalDateTime createdAt;
        private String description;
        private Long id;
        private LocalDateTime updatedAt;

        private String updatedBy;
        private Long version;

        private RoleBuilder() {
        }

        public static RoleBuilder aRole() {
            return new RoleBuilder();
        }

        public Role build() {
            Role role = new Role(code);
            role.setDescription(description);
            role.setCreatedAt(createdAt);
            role.setId(id);
            role.setUpdatedAt(updatedAt);
            role.setUpdatedBy(updatedBy);
            role.setVersion(version);
            return role;
        }

        public RoleBuilder withCode(RoleCode code) {
            this.code = code;
            return this;
        }

        public RoleBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RoleBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RoleBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public RoleBuilder withUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RoleBuilder withUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public RoleBuilder withVersion(Long version) {
            this.version = version;
            return this;
        }
    }
}
