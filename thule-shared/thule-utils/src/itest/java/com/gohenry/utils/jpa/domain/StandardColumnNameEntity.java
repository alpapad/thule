package com.gohenry.utils.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StandardColumnNameEntity {

    @Id
    private String id;

    private String someProperty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSomeProperty() {
        return someProperty;
    }

    public void setSomeProperty(String someProperty) {
        this.someProperty = someProperty;
    }

}
