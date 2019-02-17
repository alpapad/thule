package uk.co.serin.thule.repository.mongodb.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@ToString
public final class PersonEntity {
    public static final int EMAIL_ADDRESS_MAX_LENGTH = 100;
    public static final int FIRST_NAME_MAX_LENGTH = 30;
    public static final int LAST_NAME_MAX_LENGTH = 30;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int SECOND_NAME_MAX_LENGTH = 30;
    public static final int TITLE_MAX_LENGTH = 10;
    public static final int USER_ID_MAX_LENGTH = 100;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @NotNull
    private LocalDate dateOfBirth;

    @Builder.Default
    @NotNull
    private LocalDate dateOfExpiry = LocalDate.now().plusYears(1);

    @Builder.Default
    @NotNull
    private LocalDate dateOfPasswordExpiry = LocalDate.now().plusYears(1);

    @NotEmpty
    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    @Size(max = EMAIL_ADDRESS_MAX_LENGTH)
    private String emailAddress;

    @NotEmpty
    @Size(max = FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @Id
    private Long id;

    @NotEmpty
    @Size(max = LAST_NAME_MAX_LENGTH)
    private String lastName;

    @NotEmpty
    @Size(max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = SECOND_NAME_MAX_LENGTH)
    private String secondName;

    @Size(max = TITLE_MAX_LENGTH)
    private String title;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    @EqualsAndHashCode.Include
    @NotEmpty
    @Size(max = USER_ID_MAX_LENGTH)
    private String userId;

    @Version
    private long version;
}