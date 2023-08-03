package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Entity(name = "employees")
public class Employee {

    @jakarta.persistence.Id
    @GeneratedValue
    private long id;
    private String name;
    private long version = 0L;

    public Employee() {
    }


    //getter setters

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
