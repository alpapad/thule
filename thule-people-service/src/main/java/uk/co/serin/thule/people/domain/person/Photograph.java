package uk.co.serin.thule.people.domain.person;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Transient;

import uk.co.serin.thule.people.domain.DomainModel;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
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
    private static final String SHA = "SHA";
    private static final long serialVersionUID = -7142100183643772609L;

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
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    Photograph() {
    }

    /**
     * Copy object constructor
     * @param person Object to be copied
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Photograph(Photograph photograph, Person person) {
        // Copy business key
        this.person = person;
        setPhoto(photograph.getPhoto());
        // Copy mutable properties, i.e. those with a setter
        BeanUtils.copyProperties(photograph, this);
        // Copy immutable properties, i.e. those without a setter
        getAudit().setCreatedAt(photograph.getAudit().getCreatedAt());
        getAudit().setUpdatedAt(photograph.getAudit().getUpdatedAt());
        getAudit().setUpdatedBy(photograph.getAudit().getUpdatedBy());
    }

    /**
     * Business key constructor
     * @param photo Business key attribute
     * @param person key attribute
     */
    @SuppressWarnings("squid:S2637") // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Photograph(byte[] photo, Person person) {
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
        return Arrays.copyOf(photo, photo.length);
    }

    private void setPhoto(byte... photo) {
        byte[] photoToSet = Arrays.copyOf(photo, photo.length);
        try {
            byte[] digestedPhoto = MessageDigest.getInstance(SHA).digest(photoToSet);
            setHash(new String(Base64.getEncoder().encode(digestedPhoto), Charset.defaultCharset()));
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        }

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
                .add(String.format("hash=%s", hash))
                .add(String.format("photo=%s", new Object[]{photo}))
                .add(String.format("position=%s", position))
                .toString();
    }
}
