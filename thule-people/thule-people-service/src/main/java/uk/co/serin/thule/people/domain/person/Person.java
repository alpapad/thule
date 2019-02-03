package uk.co.serin.thule.people.domain.person;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.address.HomeAddress;
import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.state.State;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = DomainModel.ENTITY_NAME_PEOPLE)
@ToString(callSuper = true)
public class Person extends DomainModel {
    public static final int EMAIL_ADDRESS_MAX_LENGTH = 100;
    public static final String EXCLUDE_CREDENTIALS_FILTER = "excludePasswordFilter";
    public static final int FIRST_NAME_MAX_LENGTH = 30;
    public static final int LAST_NAME_MAX_LENGTH = 30;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int SECOND_NAME_MAX_LENGTH = 30;
    public static final int TITLE_MAX_LENGTH = 10;
    public static final int USER_ID_MAX_LENGTH = 100;

    @Builder.Default
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = ENTITY_NAME_PERSON, orphanRemoval = true)
    @ToString.Exclude
    private Set<Photograph> photographs = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(name = DATABASE_TABLE_PEOPLE_ROLES,
            joinColumns = {@JoinColumn(name = DATABASE_COLUMN_PERSON_ID, nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = DATABASE_COLUMN_ROLE_ID, nullable = false)})
    @NotNull
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    @NotNull
    private LocalDate dateOfBirth;

    private LocalDate dateOfExpiry = LocalDate.now().plusYears(1);

    private LocalDate dateOfPasswordExpiry = LocalDate.now().plusYears(1);

    @NotEmpty
    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    @Size(max = EMAIL_ADDRESS_MAX_LENGTH)
    private String emailAddress;

    @NotEmpty
    @Size(max = FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = DATABASE_COLUMN_HOME_ADDRESS_ID)
    @ToString.Exclude
    private HomeAddress homeAddress;

    @NotEmpty
    @Size(max = LAST_NAME_MAX_LENGTH)
    private String lastName;

    @NotEmpty
    @Size(max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = SECOND_NAME_MAX_LENGTH)
    private String secondName;

    @ManyToOne(optional = false)
    @JoinColumn(name = DATABASE_COLUMN_STATE_ID, nullable = false)
    @NotNull
    @ToString.Exclude
    private State state;

    @Size(max = TITLE_MAX_LENGTH)
    private String title;

    @EqualsAndHashCode.Include
    @NotEmpty
    @Size(max = USER_ID_MAX_LENGTH)
    private String userId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = DATABASE_COLUMN_WORK_ADDRESS_ID)
    @ToString.Exclude
    private WorkAddress workAddress;
}