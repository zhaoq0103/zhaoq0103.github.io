package com.x.ai.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Table;

@Table(name="t_users")
public class User extends AbstractEntity {

    private String email;
    private String password;
    private String name;

    public User(){}

    public User(long id, String email, String password, String name) {
        setId(id);
        setEmail(email);
        setPassword(password);
        setName(name);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return String.format("User[id=%s, email=%s, name=%s, password=%s, createdAt=%s, createdDateTime=%s, updatedDateTime=%s]", getId(), getEmail(), getName(), getPassword(),
                getCreatedAt(), getCreatedDateTime(),getUpdatedDateTime());
    }
}
