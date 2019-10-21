package uk.co.serin.thule.people.domain.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
@ToString
public abstract class AuditEntity {
    private static final int CREATED_BY_MAX_LENGTH = 100;
    private static final int UPDATED_BY_MAX_LENGTH = 100;

    @CreatedDate
    @NotNull
    private LocalDateTime createdAt;

    @CreatedBy
    @NotEmpty
    @Size(max = CREATED_BY_MAX_LENGTH)
    private String createdBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @LastModifiedDate
    @NotNull
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @NotEmpty
    @Size(max = UPDATED_BY_MAX_LENGTH)
    private String updatedBy;

    @Version
    private long version;
}
