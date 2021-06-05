package com.example.bookmark.data;

import com.example.bookmark.service.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookmarkEntityRepository extends JpaRepository<BookmarkEntity, Long> {

    Optional<BookmarkEntity> findOneBookmarkByIdentifier(UUID identifier);

    void deleteBookmarkEntityByIdentifier(UUID identifier);

    List<BookmarkEntity> findAllBookmarksByUserIdentifier(UUID userIdentifier);

    List<BookmarkEntity> findAllBookmarksByCategory(String category);

    List<BookmarkEntity> findAllBookmarksByName(String name);
}
