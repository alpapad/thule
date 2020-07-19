package uk.co.serin.thule.people.domain.entity.person;

import org.springframework.data.annotation.Transient;

import uk.co.serin.thule.people.domain.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Getter
@Setter
@Table(name = "accounts")
@ToString(callSuper = true)
public class AccountEntity extends AuditEntity {
    private static final int HASH_MAX_LENGTH = 255;

    @EqualsAndHashCode.Include
    @Column(name = "account_number")
    private int number;

    @ManyToOne(optional = false)
    @JoinColumn(name = "personId", nullable = false, updatable = false)
    @NotNull
    @Transient
    @ToString.Exclude
    private PersonEntity person;
}
