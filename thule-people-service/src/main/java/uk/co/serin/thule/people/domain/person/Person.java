package uk.co.serin.thule.people.domain.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.util.Assert;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.address.HomeAddress;
import uk.co.serin.thule.people.domain.address.WorkAddress;
import uk.co.serin.thule.people.domain.role.Role;
import uk.co.serin.thule.people.domain.state.Action;
import uk.co.serin.thule.people.domain.state.ActionCode;
import uk.co.serin.thule.people.domain.state.State;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

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
    public static final int LAST_NAME_MAX_LENGTH = 30;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int SECOND_NAME_MAX_LENGTH = 30;
    public static final int TITLE_MAX_LENGTH = 10;
    public static final int USER_ID_MAX_LENGTH = 100;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = ENTITY_NAME_PERSON, orphanRemoval = true)
    private final Set<Photograph> photographs = new HashSet<>();

    @ManyToMany
    @JoinTable(name = DATABASE_TABLE_PEOPLE_ROLES,
            joinColumns = {@JoinColumn(name = DATABASE_COLUMN_PERSON_ID, nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = DATABASE_COLUMN_ROLE_ID, nullable = false)})
    @NotNull
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
    private HomeAddress homeAddress;

    @Column(length = LAST_NAME_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = LAST_NAME_MAX_LENGTH)
    private String lastName;

    @Column(length = PASSWORD_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = PASSWORD_MAX_LENGTH)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String password;

    @Column(length = SECOND_NAME_MAX_LENGTH)
    @Size(max = SECOND_NAME_MAX_LENGTH)
    private String secondName;

    @ManyToOne(optional = false)
    @JoinColumn(name = DATABASE_COLUMN_STATE_ID, nullable = false)
    @NotNull
    @JsonIgnore
    private State state;

    @Column(length = TITLE_MAX_LENGTH)
    @Size(max = TITLE_MAX_LENGTH)
    private String title;

    @Column(length = USER_ID_MAX_LENGTH, nullable = false, unique = true)
    @NotNull
    @Size(max = USER_ID_MAX_LENGTH)
    private String userId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = DATABASE_COLUMN_WORK_ADDRESS_ID)
    private WorkAddress workAddress;

    /**
     * Default constructor required when instantiating as java bean, e.g. by hibernate or jackson
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    Person() {
        initialise();
    }

    private void initialise() {
        LocalDate defaultExpiry = LocalDate.now().plusYears(1);

        dateOfExpiry = defaultExpiry;
        dateOfPasswordExpiry = defaultExpiry;
        password = userId;
    }

    /**
     * Business key constructor
     *
     * @param userId Business key attribute
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Person(String userId) {
        Assert.hasText(userId, "userId must have text");
        this.userId = userId;
        initialise();
    }

    public void addPhotographs(Set<Photograph> photographs) {
        this.photographs.addAll(photographs);
    }

    public void addRoles(Set<Role> roles) {
        this.roles.addAll(roles);
    }

    public void disable() {
        // Validate the action is valid for the current state
        if (!getState().getActionsByCode().containsKey(ActionCode.PERSON_DISABLE)) {
            throw new PersonInvalidStateException(this);
        }

        // Set new state
        Action personViewAction = getState().getActionsByCode().get(ActionCode.PERSON_DISABLE);
        state = personViewAction.getNextState();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void discard() {
        // Validate the action is valid for the current state
        if (!getState().getActionsByCode().containsKey(ActionCode.PERSON_DISCARD)) {
            throw new PersonInvalidStateException(this);
        }

        // Set new state
        Action personViewAction = getState().getActionsByCode().get(ActionCode.PERSON_DISCARD);
        state = personViewAction.getNextState();
    }

    public void enable() {
        // Validate the action is valid for the current state
        if (!getState().getActionsByCode().containsKey(ActionCode.PERSON_ENABLE)) {
            throw new PersonInvalidStateException(this);
        }

        // Set new state
        Action personViewAction = getState().getActionsByCode().get(ActionCode.PERSON_ENABLE);
        state = personViewAction.getNextState();
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        state = personViewAction.getNextState();
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
                .add(String.format("lastName=%s", lastName))
                .add(String.format("secondName=%s", secondName))
                .add(String.format("state=%s", state))
                .add(String.format("title=%s", title))
                .add(String.format("userId=%s", userId))
                .toString();
    }

    public void update() {
        // Validate the action is valid for the current state
        if (!getState().getActionsByCode().containsKey(ActionCode.PERSON_UPDATE)) {
            throw new PersonInvalidStateException(this);
        }
    }

    public static final class PersonBuilder {
        protected Long version;
        private LocalDateTime createdAt;
        private LocalDate dateOfBirth;
        private LocalDate dateOfExpiry;
        private LocalDate dateOfPasswordExpiry;
        private String emailAddress;
        private String firstName;
        private HomeAddress homeAddress;
        private Long id;
        private String lastName;
        private String password;
        private Set<Photograph> photographs = new HashSet<>();
        private Set<Role> roles = new HashSet<>();
        private String secondName;
        private State state;
        private String title;
        private LocalDateTime updatedAt;
        private String updatedBy;
        private String userId;
        private WorkAddress workAddress;

        private PersonBuilder() {
        }

        public static PersonBuilder aPerson() {
            return new PersonBuilder();
        }

        public Person build() {
            Person person = new Person(userId);
            person.setCreatedAt(createdAt);
            person.setDateOfBirth(dateOfBirth);
            person.setDateOfExpiry(dateOfExpiry);
            person.setDateOfPasswordExpiry(dateOfPasswordExpiry);
            person.setEmailAddress(emailAddress);
            person.setFirstName(firstName);
            person.setHomeAddress(homeAddress);
            person.setId(id);
            person.setPassword(password);
            person.setTitle(title);
            person.setSecondName(secondName);
            person.setState(state);
            person.setLastName(lastName);
            person.setUpdatedAt(updatedAt);
            person.setUpdatedBy(updatedBy);
            person.setVersion(version);
            person.setWorkAddress(workAddress);
            person.addRoles(this.roles);
            person.addPhotographs(this.photographs);
            return person;
        }

        public PersonBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PersonBuilder withDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public PersonBuilder withDateOfExpiry(LocalDate dateOfExpiry) {
            this.dateOfExpiry = dateOfExpiry;
            return this;
        }

        public PersonBuilder withDateOfPasswordExpiry(LocalDate dateOfPasswordExpiry) {
            this.dateOfPasswordExpiry = dateOfPasswordExpiry;
            return this;
        }

        public PersonBuilder withEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public PersonBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PersonBuilder withHomeAddress(HomeAddress homeAddress) {
            this.homeAddress = homeAddress;
            return this;
        }

        public PersonBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PersonBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PersonBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public PersonBuilder withPhotographs(Set<Photograph> photographs) {
            this.photographs = photographs;
            return this;
        }

        public PersonBuilder withRoles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public PersonBuilder withSecondName(String secondName) {
            this.secondName = secondName;
            return this;
        }

        public PersonBuilder withState(State state) {
            this.state = state;
            return this;
        }

        public PersonBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public PersonBuilder withUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }


        public PersonBuilder withUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public PersonBuilder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public PersonBuilder withVersion(Long version) {
            this.version = version;
            return this;
        }

        public PersonBuilder withWorkAddress(WorkAddress workAddress) {
            this.workAddress = workAddress;
            return this;
        }
    }
}