package com.gohenry.utils.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StandardTableNameEntity {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
