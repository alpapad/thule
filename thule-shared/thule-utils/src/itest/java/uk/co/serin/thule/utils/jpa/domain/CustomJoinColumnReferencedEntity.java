package uk.co.serin.thule.utils.jpa.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CustomJoinColumnReferencedEntity {

    @Id
    private String id;

    @OneToMany(mappedBy = "referencedEntity", fetch = FetchType.EAGER)
    private List<CustomJoinColumnOwnerEntity> ownerEntities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CustomJoinColumnOwnerEntity> getOwnerEntities() {
        return ownerEntities;
    }

    public void setOwnerEntities(List<CustomJoinColumnOwnerEntity> ownerEntities) {
        this.ownerEntities = ownerEntities;
    }

}
