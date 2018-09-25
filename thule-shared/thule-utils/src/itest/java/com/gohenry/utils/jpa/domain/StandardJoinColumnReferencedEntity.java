package com.gohenry.utils.jpa.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class StandardJoinColumnReferencedEntity {

    @Id
    private String id;

    @OneToMany(mappedBy = "referencedEntity", fetch = FetchType.EAGER)
    private List<StandardJoinColumnOwnerEntity> ownerEntities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<StandardJoinColumnOwnerEntity> getOwnerEntities() {
        return ownerEntities;
    }

    public void setOwnerEntities(List<StandardJoinColumnOwnerEntity> ownerEntities) {
        this.ownerEntities = ownerEntities;
    }

}
