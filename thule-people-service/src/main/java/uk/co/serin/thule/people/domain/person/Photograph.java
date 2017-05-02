package uk.co.serin.thule.people.domain.person;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.BeanUtils;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = DomainModel.ENTITY_NAME_PHOTOGRAPHS)
public final class Photograph extends DomainModel {
    private static final int HASH_MAX_LENGTH = 255;
    private static final int PHOTO_MAX_LENGTH = 4096;

    @Column(length = HASH_MAX_LENGTH, nullable = false)
    @NotNull
    @Size(max = HASH_MAX_LENGTH)
    private String hash;

    @ManyToOne(optional = false)
    @JoinColumn(name = ENTITY_ATTRIBUTE_NAME_PERSON_ID, nullable = false, updatable = false)
    @NotNull
    @Transient
    @JsonIgnore
    private Person person;

    @Column(length = PHOTO_MAX_LENGTH, nullable = false)
    @Lob
    @NotNull
    private byte[] photo;

    @Column(name = DATABASE_COLUMN_POSITIN, nullable = false)
    // Don't call this position because 'position' is a reserved word in HSQL
    @NotNull
    private long position;

    /**
     * Default constructor required by Hibernate
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    Photograph() {
    }

    /**
     * Copy object constructor
     *
     * @param person Object to be copied
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Photograph(Photograph photograph, Person person) {
        // Copy mutable inherited properties
        super(photograph);
        // Copy business key
        this.person = person;
        setPhoto(photograph.getPhoto());
        // Copy mutable properties
        BeanUtils.copyProperties(photograph, this);
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
        setPhoto(photo);
        this.person = person;
    }

    public byte[] getPhoto() {
        return Arrays.copyOf(photo, photo.length);
    }

    private Photograph setPhoto(byte... photo) {
        byte[] photoToSet = Arrays.copyOf(photo, photo.length);
        setHash(new String(DigestUtils.md5Digest(photoToSet), Charset.defaultCharset()));

        this.photo = photoToSet;
        return this;
    }

    public String getHash() {
        return hash;
    }

    private Photograph setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public Person getPerson() {
        return person;
    }

    public long getPosition() {
        return position;
    }

    public Photograph setPosition(long positin) {
        this.position = positin;
        return this;
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
                .add(String.format("photo=%s", new Object[]{photo}))
                .add(String.format("position=%s", position))
                .toString();
    }
}
