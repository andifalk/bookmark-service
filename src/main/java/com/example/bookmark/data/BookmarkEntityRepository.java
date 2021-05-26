package com.example.bookmark.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkEntityRepository extends JpaRepository<BookmarkEntity, Long> {

    void deleteBookmarkEntityByIdentifier(String identifier);
}
