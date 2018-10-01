package uk.co.serin.thule.utils.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CustomJoinColumnOwnerEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "customForeign_key_id")
    private CustomJoinColumnReferencedEntity referencedEntity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomJoinColumnReferencedEntity getReferencedEntity() {
        return referencedEntity;
    }

    public void setReferencedEntity(CustomJoinColumnReferencedEntity referencedEntity) {
        this.referencedEntity = referencedEntity;
    }

}