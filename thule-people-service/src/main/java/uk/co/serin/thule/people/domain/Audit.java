package uk.co.serin.thule.people.domain;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Embeddable
public final class Audit implements Serializable {
    public static final String ENTITY_ATTRIBUTE_NAME_CREATED_AT = "createdAt";
    public static final String ENTITY_ATTRIBUTE_NAME_UPDATED_AT = "updatedAt";
    public static final String ENTITY_ATTRIBUTE_NAME_UPDATED_BY = "updatedBy";
    private static final int UPDATED_BY_MAX_LENGTH = 100;
    private static final long serialVersionUID = -2064527752664817053L;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column(length = UPDATED_BY_MAX_LENGTH)
    private String updatedBy;

    public Audit(Audit audit) {
        // Copy business key
        // Copy mutable properties, i.e. those with a setter
        BeanUtils.copyProperties(audit, this);
        // Copy immutable properties, i.e. those without a setter
    }

    public Audit() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, updatedAt, updatedBy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Audit audit = (Audit) o;
        return Objects.equals(createdAt, audit.createdAt) &&
                Objects.equals(updatedAt, audit.updatedAt) &&
                Objects.equals(updatedBy, audit.updatedBy);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Audit{", "}")
                .add(String.format("createdAt=%s", createdAt))
                .add(String.format("updatedAt=%s", updatedAt))
                .add(String.format("updatedBy=%s", updatedBy))
                .toString();
    }

    @PrePersist
    public void initialise() {
        Assert.notNull(SecurityContextHolder.getContext().getAuthentication(), "Current user is not authenticated");
        Assert.notNull(SecurityContextHolder.getContext().getAuthentication().getName(), "Current user is not authenticated");

        createdAt = LocalDateTime.now();
        updatedAt = getCreatedAt();
        updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PreUpdate
    public void update() {
        Assert.notNull(SecurityContextHolder.getContext().getAuthentication(), "Current user is not authenticated");
        Assert.notNull(SecurityContextHolder.getContext().getAuthentication().getName(), "Current user is not authenticated");

        updatedAt = LocalDateTime.now();
        updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
