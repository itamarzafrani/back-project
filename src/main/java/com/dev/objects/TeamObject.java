package com.dev.objects;

import javax.persistence.*;

@Entity
@Table(name = "teams")
public class TeamObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name;


    public TeamObject(String name) {
        this.id = this.getId();
        this.name = name;
    }

    public TeamObject() {

    }

    public TeamObject(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
