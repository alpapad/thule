package uk.co.serin.thule.people.domain.person;

import org.springframework.data.annotation.Transient;
import org.springframework.util.DigestUtils;

import uk.co.serin.thule.people.domain.DomainModel;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = DomainModel.ENTITY_NAME_PHOTOGRAPHS)
public final class Photograph extends DomainModel {
    private static final int HASH_MAX_LENGTH = 255;

    @Column
    @NotNull
    @Size(max = HASH_MAX_LENGTH)
    private String hash;

    @ManyToOne(optional = false)
    @JoinColumn(name = ENTITY_ATTRIBUTE_NAME_PERSON_ID, nullable = false, updatable = false)
    @NotNull
    @Transient
    private Person person;

    @Column
    @Lob
    @NotNull
    private byte[] photo;

    @Column(name = DATABASE_COLUMN_POSITIN, nullable = false)
    // Don't call this position because 'position' is a reserved word in HSQL
    @NotNull
    private long position;

    /**
     * Default constructor required when instantiating as java bean, e.g. by hibernate or jackson
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    Photograph() {
    }

    /**
     * Business key constructor
     *
     * @param photo  Business key attribute
     * @param person key attribute
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Photograph(byte[] photo, Person person) {
        if (person != null) {
            this.person = person;
        } else {
            throw new ValidationException("The 'person' is mandatory");
        }

        setPhoto(photo);
        this.person = person;
    }

    public String getHash() {
        return hash;
    }

    private void setHash(String hash) {
        this.hash = hash;
    }

    public Person getPerson() {
        return person;
    }

    public byte[] getPhoto() {
        return (photo == null) ? null : Arrays.copyOf(photo, photo.length);
    }

    private void setPhoto(byte[] photo) {
        if (photo != null) {
            this.photo = photo;
        } else {
            throw new ValidationException("The 'photo' cannot be empty");
        }

        byte[] photoToSet = Arrays.copyOf(photo, photo.length);
        setHash(new String(DigestUtils.md5Digest(photoToSet), Charset.defaultCharset()));

        this.photo = photoToSet;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long positin) {
        this.position = positin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Photograph that = (Photograph) o;
        return Objects.equals(hash, that.hash) &&
                Objects.equals(person, that.person);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Photograph{", "}")
                .add(super.toString())
                .add(String.format("hash=%s", hash))
                .add(String.format("photo=%s", photo))
                .add(String.format("position=%s", position))
                .toString();
    }

    public static final class PhotographBuilder {
        private Person person;
        private byte[] photo;
        // Don't call this position because 'position' is a reserved word in HSQL
        private long position;

        private PhotographBuilder() {
        }

        public static PhotographBuilder aPhotograph() {
            return new PhotographBuilder();
        }

        public Photograph build() {
            Photograph photograph = new Photograph(photo, person);
            photograph.setPosition(position);
            return photograph;
        }

        public PhotographBuilder withPerson(Person person) {
            this.person = person;
            return this;
        }

        public PhotographBuilder withPhoto(byte[] photo) {
            this.photo = photo;
            return this;
        }

        public PhotographBuilder withPosition(long position) {
            this.position = position;
            return this;
        }
    }
}
