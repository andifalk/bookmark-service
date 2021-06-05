package com.example.bookmark.data;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URL;
import java.util.UUID;

@Entity
public class BookmarkEntity extends AbstractPersistable<Long> {

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

    public BookmarkEntity() {
    }

    public BookmarkEntity(UUID identifier, String name, String description, String category, URL url, UUID userIdentifier) {
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public URL getUrl() {
        return url;
    }

    public UUID getUserIdentifier() {
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
