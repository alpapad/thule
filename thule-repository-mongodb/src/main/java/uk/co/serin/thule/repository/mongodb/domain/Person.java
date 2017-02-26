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

@SuppressWarnings("common-java:DuplicatedBlocks")
// Suppress SonarQube code smell Source files should not have any duplicated blocks
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
    Person() {
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

    public String getUserId() {
        return userId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Person setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public LocalDate getDateOfExpiry() {
        return dateOfExpiry;
    }

    public Person setDateOfExpiry(LocalDate dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
        return this;
    }

    public LocalDate getDateOfPasswordExpiry() {
        return dateOfPasswordExpiry;
    }

    public Person setDateOfPasswordExpiry(LocalDate dateOfPasswordExpiry) {
        this.dateOfPasswordExpiry = dateOfPasswordExpiry;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Person setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Person setId(Long id) {
        this.id = id;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Person setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSalutation() {
        return salutation;
    }

    public Person setSalutation(String salutation) {
        this.salutation = salutation;
        return this;
    }

    public String getSecondName() {
        return secondName;
    }

    public Person setSecondName(String secondName) {
        this.secondName = secondName;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public Person setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Person setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Person setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Person setVersion(Long version) {
        this.version = version;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Person setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
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

    private void initialise() {
        LocalDate defaultExpiry = LocalDate.now().plusYears(1);

        dateOfExpiry = defaultExpiry;
        dateOfPasswordExpiry = defaultExpiry;
        password = userId;
    }
}
