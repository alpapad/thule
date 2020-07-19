package uk.co.serin.thule.people.domain.entity.person;

import uk.co.serin.thule.people.domain.entity.AuditEntity;
import uk.co.serin.thule.people.domain.entity.address.HomeAddressEntity;
import uk.co.serin.thule.people.domain.entity.address.WorkAddressEntity;
import uk.co.serin.thule.people.domain.entity.role.RoleEntity;
import uk.co.serin.thule.people.domain.entity.state.StateEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "people")
@ToString(callSuper = true)
public class PersonEntity extends AuditEntity {
    public static final int EMAIL_ADDRESS_MAX_LENGTH = 100;
    public static final int FIRST_NAME_MAX_LENGTH = 30;
    public static final int LAST_NAME_MAX_LENGTH = 30;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int SECOND_NAME_MAX_LENGTH = 30;
    public static final int TITLE_MAX_LENGTH = 10;
    public static final int USER_ID_MAX_LENGTH = 100;

    @Builder.Default
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "person", orphanRemoval = true)
    @ToString.Exclude
    private Set<AccountEntity> accounts = new HashSet<>();

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

    @JoinColumn(name = "home_address_id")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @ToString.Exclude
    private HomeAddressEntity homeAddress;

    @NotEmpty
    @Size(max = LAST_NAME_MAX_LENGTH)
    private String lastName;

    @NotEmpty
    @Size(max = PASSWORD_MAX_LENGTH)
    private String password;

    @Lob
    private byte[] photograph;

    @Builder.Default
    @JoinTable(name = "people_roles",
            joinColumns = {@JoinColumn(name = "person_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role_id", nullable = false)})
    @ManyToMany
    @NotNull
    @ToString.Exclude
    private Set<RoleEntity> roles = new HashSet<>();

    @Size(max = SECOND_NAME_MAX_LENGTH)
    private String secondName;

    @JoinColumn(name = "state_id", nullable = false)
    @ManyToOne(optional = false)
    @NotNull
    @ToString.Exclude
    private StateEntity state;

    @Size(max = TITLE_MAX_LENGTH)
    private String title;

    @EqualsAndHashCode.Include
    @NotEmpty
    @Size(max = USER_ID_MAX_LENGTH)
    private String userId;

    @JoinColumn(name = "work_address_id")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @ToString.Exclude
    private WorkAddressEntity workAddress;
}