package com.gohenry.utils.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StandardJoinColumnOwnerEntity {

    @Id
    private String id;

    @ManyToOne
    private StandardJoinColumnReferencedEntity referencedEntity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StandardJoinColumnReferencedEntity getReferencedEntity() {
        return referencedEntity;
    }

    public void setReferencedEntity(StandardJoinColumnReferencedEntity referencedEntity) {
        this.referencedEntity = referencedEntity;
    }

}
