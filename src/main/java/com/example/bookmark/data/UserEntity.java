package com.example.bookmark.data;

import org.springframework.data.jpa.domain.AbstractPersistable;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Entity
public class UserEntity extends AbstractPersistable<Long> {

    private String identifier;

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    @ElementCollection(fetch = EAGER)
    private List<String> roles = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(String identifier, String firstName, String lastName, String password, String email, List<String> roles) {
        this.identifier = identifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "identifier=" + identifier +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                "} " + super.toString();
    }
}
