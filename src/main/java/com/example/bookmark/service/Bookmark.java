package com.example.bookmark.service;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.net.URL;
import java.util.UUID;

public class Bookmark {

    @NotNull
    private UUID identifier;

    @NotEmpty
    private String name;

    @Size(max = 500)
    private String description;

    @NotEmpty
    private String category;

    @NotNull
    private URL url;

    @NotNull
    private UUID userIdentifier;

    public Bookmark() {
    }

    public Bookmark(UUID identifier, String name, String description, String category, URL url, UUID userIdentifier) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.category = category;
        this.url = url;
        this.userIdentifier = userIdentifier;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public UUID getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(UUID userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", url='" + url + '\'' +
                ", userIdentifier='" + userIdentifier + '\'' +
                '}';
    }
}
