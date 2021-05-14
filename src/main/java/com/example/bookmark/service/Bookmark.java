package com.example.bookmark.service;

import java.util.UUID;

public class Bookmark {

    private String identifier;
    private String name;
    private String description;
    private String category;
    private String url;
    private String userIdentifier;

    public Bookmark() {}

    public Bookmark(String identifier, String name, String description, String category, String url, String userIdentifier) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.category = category;
        this.url = url;
        this.userIdentifier = userIdentifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
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
