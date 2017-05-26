package uk.co.serin.thule.repository.mongodb.domain;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public final class Person {
    public static final int EMAIL_ADDRESS_MAX_LENGTH = 100;
    public static final String ENTITY_ATTRIBUTE_NAME_CREATED_AT = "createdAt";
    public static final String ENTITY_ATTRIBUTE_NAME_UPDATED_AT = "updatedAt";
    public static final String ENTITY_ATTRIBUTE_NAME_UPDATED_BY = "updatedBy";
    public static final String ENTITY_ATTRIBUTE_NAME_USER_ID = "userId";
    public static final int FIRST_NAME_MAX_LENGTH = 30;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int SALUTATION_MAX_LENGTH = 10;
    public static final int SECOND_NAME_MAX_LENGTH = 30;
    public static final int SURNAME_MAX_LENGTH = 30;
    public static final int USER_ID_MAX_LENGTH = 100;

    @NotNull
    @Size(max = USER_ID_MAX_LENGTH)
    private final String userId;

    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private LocalDate dateOfExpiry;

    @NotNull
    private LocalDate dateOfPasswordExpiry;

    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    @NotNull
    @Size(max = EMAIL_ADDRESS_MAX_LENGTH)
    private String emailAddress;

    @NotNull
    @Size(max = FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @Id
    private Long id;

    @NotNull
    @Size(max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = SALUTATION_MAX_LENGTH)
    private String salutation;

    @Size(max = SECOND_NAME_MAX_LENGTH)
    private String secondName;

    @NotNull
    @Size(max = SURNAME_MAX_LENGTH)
    private String surname;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    @Version
    private Long version;

    /**
     * Required by MongoDb
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    protected Person() {
        this("");
    }

    /**
     * Business key constructor
     *
     * @param userId Business key attribute
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Person(String userId) {
        this.userId = userId;
        initialise();
    }

    /**
     * Copy object constructor
     *
     * @param person Object to be copied
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Person(Person person) {
        // Copy business key
        this.userId = person.userId;
        // Copy mutable properties
        BeanUtils.copyProperties(person, this);
    }

    private void initialise() {
        LocalDate defaultExpiry = LocalDate.now().plusYears(1);

        dateOfExpiry = defaultExpiry;
        dateOfPasswordExpiry = defaultExpiry;
        password = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getUserId() {
        return userId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", "Person{", "}")
                .add(String.format("createdAt=%s", createdAt))
                .add(String.format("dateOfBirth=%s", dateOfBirth))
                .add(String.format("dateOfExpiry=%s", dateOfExpiry))
                .add(String.format("dateOfPasswordExpiry=%s", dateOfPasswordExpiry))
                .add(String.format("emailAddress=%s", emailAddress))
                .add(String.format("firstName=%s", firstName))
                .add(String.format("id=%s", id))
                .add(String.format("salutation=%s", salutation))
                .add(String.format("secondName=%s", secondName))
                .add(String.format("surname=%s", surname))
                .add(String.format("updatedAt=%s", updatedAt))
                .add(String.format("updatedBy=%s", updatedBy))
                .add(String.format("userId=%s", userId))
                .add(String.format("version=%s", version))
                .toString();
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(dateOfExpiry);
    }

    public boolean isPasswordExpired() {
        return LocalDate.now().isAfter(dateOfPasswordExpiry);
    }

    public static final class PersonBuilder {
        private LocalDateTime createdAt;
        private LocalDate dateOfBirth;
        private LocalDate dateOfExpiry;
        private LocalDate dateOfPasswordExpiry;
        private String emailAddress;
        private String firstName;
        private Long id;
        private String password;
        private String salutation;
        private String secondName;
        private String surname;
        private LocalDateTime updatedAt;
        private String updatedBy;
        private String userId;
        private Long version;

        private PersonBuilder() {
        }

        public static PersonBuilder aPerson() {
            return new PersonBuilder();
        }

        public Person build() {
            Person person = new Person(userId);
            person.updatedAt = this.updatedAt;
            person.emailAddress = this.emailAddress;
            person.id = this.id;
            person.createdAt = this.createdAt;
            person.dateOfPasswordExpiry = this.dateOfPasswordExpiry;
            person.dateOfBirth = this.dateOfBirth;
            person.version = this.version;
            person.salutation = this.salutation;
            person.firstName = this.firstName;
            person.dateOfExpiry = this.dateOfExpiry;
            person.surname = this.surname;
            person.secondName = this.secondName;
            person.password = this.password;
            person.updatedBy = this.updatedBy;
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

        public PersonBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PersonBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public PersonBuilder withSalutation(String salutation) {
            this.salutation = salutation;
            return this;
        }

        public PersonBuilder withSecondName(String secondName) {
            this.secondName = secondName;
            return this;
        }

        public PersonBuilder withSurname(String surname) {
            this.surname = surname;
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
    }
}
