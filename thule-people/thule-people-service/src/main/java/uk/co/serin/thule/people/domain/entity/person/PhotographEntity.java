package uk.co.serin.thule.people.domain.entity.person;

import org.springframework.data.annotation.Transient;

import uk.co.serin.thule.people.domain.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Getter
@Setter
@Table(name = AuditEntity.ENTITY_NAME_PHOTOGRAPHS)
@ToString(callSuper = true)
public class PhotographEntity extends AuditEntity {
    private static final int HASH_MAX_LENGTH = 255;

    @EqualsAndHashCode.Include
    @Size(max = HASH_MAX_LENGTH)
    private String hash;

    @ManyToOne(optional = false)
    @JoinColumn(name = ENTITY_ATTRIBUTE_NAME_PERSON_ID, nullable = false, updatable = false)
    @NotNull
    @Transient
    @ToString.Exclude
    private PersonEntity person;

    @Lob
    @NotNull
    @NonNull
    private byte[] photo;

    @EqualsAndHashCode.Include
    @Column(name = DATABASE_COLUMN_POSITIN) // Don't call this position because 'position' is a reserved word in HSQL
    private long position;
}
