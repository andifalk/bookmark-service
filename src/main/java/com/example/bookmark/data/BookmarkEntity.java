package com.example.bookmark.data;

import org.springframework.data.jpa.domain.AbstractPersistable;

import jakarta.persistence.Entity;

@Entity
public class BookmarkEntity extends AbstractPersistable<Long> {

    private String identifier;
    private String name;
    private String description;
    private String category;
    private String url;
    private String userIdentifier;

    public BookmarkEntity() {
    }

    public BookmarkEntity(String identifier, String name, String description, String category, String url, String userIdentifier) {
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    @Override
    public String toString() {
        return "BookmarkEntity{" +
                "identifier=" + identifier +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", url='" + url + '\'' +
                ", userIdentifier=" + userIdentifier +
                "} " + super.toString();
    }
}
