package uk.co.serin.thule.people.domain.person;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.address.HomeAddress;
import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.State;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = DomainModel.ENTITY_NAME_PEOPLE)
public final class Person extends DomainModel {
    public static final int EMAIL_ADDRESS_MAX_LENGTH = 100;
    public static final int FIRST_NAME_MAX_LENGTH = 30;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int SALUTATION_MAX_LENGTH = 10;
    public static final int SECOND_NAME_MAX_LENGTH = 30;
    public static final int SURNAME_MAX_LENGTH = 30;
    public static final int USER_ID_MAX_LENGTH = 100;
    private static final long serialVersionUID = 6373551551159419398L;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = ENTITY_NAME_PERSON, orphanRemoval = true)
    @JsonIgnore
    private final Set<Photograph> photographs = new HashSet<>();

    @ManyToMany
    @JoinTable(name = DATABASE_TABLE_PEOPLE_ROLES,
            joinColumns = {@JoinColumn(name = DATABASE_COLUMN_PERSON_ID, nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = DATABASE_COLUMN_ROLE_ID, nullable = false)})
    @NotNull
    @JsonIgnore
    private final Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    @NotNull
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @NotNull
    private LocalDate dateOfExpiry;

    @Column(nullable = false)
    @NotNull
    private LocalDate dateOfPasswordExpiry;

    @Column(length = EMAIL_ADDRESS_MAX_LENGTH, nullable = false)
    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    @NotNull
    @Size(max = EMAIL_ADDRESS_MAX_LENGTH)
    private String emailAddress;

    @Column(length = FIRST_NAME_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = DATABASE_COLUMN_HOME_ADDRESS_ID)
    @JsonIgnore
    private HomeAddress homeAddress;

    @Column(length = PASSWORD_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = PASSWORD_MAX_LENGTH)
    private String password;

    @Column(length = SALUTATION_MAX_LENGTH)
    @Size(max = SALUTATION_MAX_LENGTH)
    private String salutation;

    @Column(length = SECOND_NAME_MAX_LENGTH)
    @Size(max = SECOND_NAME_MAX_LENGTH)
    private String secondName;

    @ManyToOne(optional = false)
    @JoinColumn(name = DATABASE_COLUMN_STATE_ID, nullable = false)
    @NotNull
    @JsonIgnore
    private State state;

    @Column(length = SURNAME_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = SURNAME_MAX_LENGTH)
    private String surname;

    @Column(length = USER_ID_MAX_LENGTH, nullable = false, unique = true)
    @NotNull
    @Size(max = USER_ID_MAX_LENGTH)
    private String userId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = DATABASE_COLUMN_WORK_ADDRESS_ID)
    @JsonIgnore
    private WorkAddress workAddress;

    /**
     * Default constructor required by Hibernate
     */
    Person() {
        initialise();
    }

    public Person(Person person) {
        // Copy business key
        this.userId = person.userId;
        // Copy mutable properties, i.e. those with a setter
        BeanUtils.copyProperties(person, this);
        setHomeAddress((person.getHomeAddress() != null) ? new HomeAddress(person.getHomeAddress()) : null);
        setWorkAddress((person.getWorkAddress() != null) ? new WorkAddress(person.getWorkAddress()) : null);
        addPhotographs(person.getPhotographs().stream().map(photograph -> new Photograph(photograph, this)));
        addRoles(person.getRoles().stream().map(Role::new));
        // Copy immutable properties, i.e. those without a setter
        getAudit().setCreatedAt(person.getAudit().getCreatedAt());
        getAudit().setUpdatedAt(person.getAudit().getUpdatedAt());
        getAudit().setUpdatedBy(person.getAudit().getUpdatedBy());
    }

    public Person(String userId) {
        Assert.hasText(userId);
        this.userId = userId;
        initialise();
    }

    public void addPhotographs(Stream<Photograph> photographs) {
        this.photographs.addAll(photographs.collect(Collectors.toList()));
    }

    public void addRoles(Stream<Role> roles) {
        this.roles.addAll(roles.collect(Collectors.toList()));
    }

    public void disable() {
        // Validate the action is valid for the current state
        if (!getState().getActionsByCode().containsKey(ActionCode.PERSON_DISABLE)) {
            throw new PersonInvalidStateException(this);
        }

        // Set new state
        Action personViewAction = getState().getActionsByCode().get(ActionCode.PERSON_DISABLE);
        setState(personViewAction.getNextState());
    }

    public void discard() {
        // Validate the action is valid for the current state
        if (!getState().getActionsByCode().containsKey(ActionCode.PERSON_DISCARD)) {
            throw new PersonInvalidStateException(this);
        }

        // Set new state
        Action personViewAction = getState().getActionsByCode().get(ActionCode.PERSON_DISCARD);
        setState(personViewAction.getNextState());
    }

    public void enable() {
        // Validate the action is valid for the current state
        if (!getState().getActionsByCode().containsKey(ActionCode.PERSON_ENABLE)) {
            throw new PersonInvalidStateException(this);
        }

        // Set new state
        Action personViewAction = getState().getActionsByCode().get(ActionCode.PERSON_ENABLE);
        setState(personViewAction.getNextState());
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(LocalDate dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public LocalDate getDateOfPasswordExpiry() {
        return dateOfPasswordExpiry;
    }

    public void setDateOfPasswordExpiry(LocalDate dateOfPasswordExpiry) {
        this.dateOfPasswordExpiry = dateOfPasswordExpiry;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public HomeAddress getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(HomeAddress homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Photograph> getPhotographs() {
        return Collections.unmodifiableSet(photographs);
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserId() {
        return userId;
    }

    public WorkAddress getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(WorkAddress workAddress) {
        this.workAddress = workAddress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(userId, person.userId);
    }

    private void initialise() {
        LocalDate defaultExpiry = LocalDate.now().plusYears(1);

        dateOfExpiry = defaultExpiry;
        dateOfPasswordExpiry = defaultExpiry;
        password = userId;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(dateOfExpiry);
    }

    public boolean isPasswordExpired() {
        return LocalDate.now().isAfter(dateOfPasswordExpiry);
    }

    public void recover() {
        // Validate the action is valid for the current state
        if (!getState().getActionsByCode().containsKey(ActionCode.PERSON_RECOVER)) {
            throw new PersonInvalidStateException(this);
        }

        // Set new state
        Action personViewAction = getState().getActionsByCode().get(ActionCode.PERSON_RECOVER);
        setState(personViewAction.getNextState());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Person{", "}")
                .add(super.toString())
                .add(String.format("dateOfBirth=%s", dateOfBirth))
                .add(String.format("dateOfExpiry=%s", dateOfExpiry))
                .add(String.format("dateOfPasswordExpiry=%s", dateOfPasswordExpiry))
                .add(String.format("emailAddress=%s", emailAddress))
                .add(String.format("firstName=%s", firstName))
                .add(String.format("homeAddress=%s", homeAddress))
                .add(String.format("photographs=%s", photographs))
                .add(String.format("roles=%s", roles))
                .add(String.format("salutation=%s", salutation))
                .add(String.format("secondName=%s", secondName))
                .add(String.format("state=%s", state))
                .add(String.format("surname=%s", surname))
                .add(String.format("userId=%s", userId))
                .add(String.format("workAddress=%s", workAddress))
                .toString();
    }

    public void update() {
        // Validate the action is valid for the current state
        if (!getState().getActionsByCode().containsKey(ActionCode.PERSON_UPDATE)) {
            throw new PersonInvalidStateException(this);
        }
    }
}