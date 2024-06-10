package com.example.bookmark.data;

import org.springframework.data.jpa.domain.AbstractPersistable;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

@Entity
public class UserEntity extends AbstractPersistable<Long> {

    @NotNull
    private UUID identifier;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String password;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    @ElementCollection(fetch = EAGER)
    private List<String> roles = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(UUID identifier, String firstName, String lastName, String password, String email, List<String> roles) {
        this.identifier = identifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public UUID getIdentifier() {
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
