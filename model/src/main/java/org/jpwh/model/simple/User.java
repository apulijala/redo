package org.jpwh.model.simple;

import java.io.Serializable;

public class User implements Serializable {


    protected Long id;

    public Long getId() {return  id;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    protected String username;


    public User() {

    }

}
