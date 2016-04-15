package uk.co.serin.thule.people.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

@MappedSuperclass
public abstract class DomainModel implements Serializable {
    public static final String DATABASE_COLUMN_ACTION_ID = "action_id";
    public static final String DATABASE_COLUMN_ADDRESS_TYPE = "address_type";
    public static final String DATABASE_COLUMN_ADDRESS_TYPE_VALUE_HOME = "HOME";
    public static final String DATABASE_COLUMN_ADDRESS_TYPE_VALUE_UNSPECIFIED = "UNSPECIFIED";
    public static final String DATABASE_COLUMN_ADDRESS_TYPE_VALUE_WORK = "WORK";
    public static final String DATABASE_COLUMN_COUNTRY_ID = "country_id";
    public static final String DATABASE_COLUMN_FIRST_NAME = "first_name";
    public static final String DATABASE_COLUMN_HOME_ADDRESS_ID = "home_address_id";
    public static final String DATABASE_COLUMN_PERSON_ID = "person_id";
    public static final String DATABASE_COLUMN_POSITIN = "positin";
    public static final String DATABASE_COLUMN_ROLE_ID = "role_id";
    public static final String DATABASE_COLUMN_STATE_ID = "state_id";
    public static final String DATABASE_COLUMN_USER_ID = "user_id";
    public static final String DATABASE_COLUMN_WORK_ADDRESS_ID = "work_address_id";
    public static final String DATABASE_SEQUENCE_UID_SEQUENCE = "UID_SEQUENCE";
    public static final String DATABASE_TABLE_PEOPLE_ROLES = "people_roles";
    public static final String DATABASE_TABLE_STATE_ACTIONS = "state_actions";
    public static final String ENTITY_ATTRIBUTE_NAME_ADDRESS_LINE_1 = "addressLine1";
    public static final String ENTITY_ATTRIBUTE_NAME_ADDRESS_LINE_2 = "addressLine2";
    public static final String ENTITY_ATTRIBUTE_NAME_AUDIT = "audit";
    public static final String ENTITY_ATTRIBUTE_NAME_CODE = "code";
    public static final String ENTITY_ATTRIBUTE_NAME_COUNTRY = "country";
    public static final String ENTITY_ATTRIBUTE_NAME_COUNTY = "county";
    public static final String ENTITY_ATTRIBUTE_NAME_CREATED_AT = "createdAt";
    public static final String ENTITY_ATTRIBUTE_NAME_DATE_OF_BIRTH = "dateOfBirth";
    public static final String ENTITY_ATTRIBUTE_NAME_DESCRIPTION = "description";
    public static final String ENTITY_ATTRIBUTE_NAME_EMAIL_ADDRESS = "emailAddress";
    public static final String ENTITY_ATTRIBUTE_NAME_FIRST_NAME = "firstName";
    public static final String ENTITY_ATTRIBUTE_NAME_HASH = "hash";
    public static final String ENTITY_ATTRIBUTE_NAME_HOME_ADDRESS = "homeAddress";
    public static final String ENTITY_ATTRIBUTE_NAME_ID = "id";
    public static final String ENTITY_ATTRIBUTE_NAME_ISO_CODE_THREE_DIGIT = "isoCodeThreeDigit";
    public static final String ENTITY_ATTRIBUTE_NAME_ISO_CODE_TW0_DIGIT = "isoCodeTwoDigit";
    public static final String ENTITY_ATTRIBUTE_NAME_ISO_NAME = "isoName";
    public static final String ENTITY_ATTRIBUTE_NAME_ISO_NUMBER = "isoNumber";
    public static final String ENTITY_ATTRIBUTE_NAME_NEXT_STATE = "nextState";
    public static final String ENTITY_ATTRIBUTE_NAME_NEXT_STATE_ID = "nextStateId";
    public static final String ENTITY_ATTRIBUTE_NAME_PERSON_ID = "personId";
    public static final String ENTITY_ATTRIBUTE_NAME_PHOTO = "photo";
    public static final String ENTITY_ATTRIBUTE_NAME_POSITIN = "positin";
    public static final String ENTITY_ATTRIBUTE_NAME_POST_CODE = "postCode";
    public static final String ENTITY_ATTRIBUTE_NAME_SALUTATION = "salutation";
    public static final String ENTITY_ATTRIBUTE_NAME_SEARCH_QUERY = "searchQuery";
    public static final String ENTITY_ATTRIBUTE_NAME_SECOND_NAME = "secondName";
    public static final String ENTITY_ATTRIBUTE_NAME_SURNAME = "surname";
    public static final String ENTITY_ATTRIBUTE_NAME_TOWN = "town";
    public static final String ENTITY_ATTRIBUTE_NAME_UPDATED_AT = "updatedAt";
    public static final String ENTITY_ATTRIBUTE_NAME_UPDATED_BY = "updatedBy";
    public static final String ENTITY_ATTRIBUTE_NAME_USER_ID = "userId";
    public static final String ENTITY_ATTRIBUTE_NAME_VERSION = "version";
    public static final String ENTITY_ATTRIBUTE_NAME_WORK_ADDRESS = "workAddress";
    public static final String ENTITY_NAME_ACTIONS = "actions";
    public static final String ENTITY_NAME_ADDRESSES = "addresses";
    public static final String ENTITY_NAME_COUNTRIES = "countries";
    public static final String ENTITY_NAME_PEOPLE = "people";
    public static final String ENTITY_NAME_PERSON = "person";
    public static final String ENTITY_NAME_PHOTOGRAPHS = "photographs";
    public static final String ENTITY_NAME_ROLES = "roles";
    public static final String ENTITY_NAME_STATE = "state";
    public static final String ENTITY_NAME_STATES = "states";
    private static final int ID_ALLOCATION_SIZE = 1;
    private static final int ID_INITIAL_VALUE = 1;

    @Transient
    private static final long serialVersionUID = 8489074283224856748L;

    @Embedded
    private final Audit audit = new Audit();

    @Id
    @SequenceGenerator(name = DATABASE_SEQUENCE_UID_SEQUENCE, sequenceName = DATABASE_SEQUENCE_UID_SEQUENCE, allocationSize = ID_ALLOCATION_SIZE, initialValue = ID_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = DATABASE_SEQUENCE_UID_SEQUENCE)
    @JsonIgnore
    private Long id;

    @Version
    @Column(nullable = false)
    @JsonIgnore
    private Long version;

    public Audit getAudit() {
        return audit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @PrePersist
    public void prePersist() {
        audit.initialise();
    }

    @PreUpdate
    public void preUpdate() {
        audit.update();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "DomainModel{", "}")
                .add(String.format("audit=%s", audit))
                .add(String.format("id=%s", id))
                .add(String.format("version=%s", version))
                .toString();
    }
}
