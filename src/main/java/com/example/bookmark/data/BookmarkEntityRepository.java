package com.example.bookmark.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookmarkEntityRepository extends JpaRepository<BookmarkEntity, Long> {

    Optional<BookmarkEntity> findOneBookmarkByIdentifierAndUserIdentifier(UUID identifier, UUID userIdentifier);

    void deleteBookmarkEntityByIdentifierAndUserIdentifier(UUID identifier, UUID userIdentifier);

    List<BookmarkEntity> findAllBookmarksByUserIdentifier(UUID userIdentifier);

    List<BookmarkEntity> findAllBookmarksByCategoryAndUserIdentifier(String category, UUID userIdentifier);

    List<BookmarkEntity> findAllBookmarksByNameAndUserIdentifier(String name, UUID userIdentifier);
}
