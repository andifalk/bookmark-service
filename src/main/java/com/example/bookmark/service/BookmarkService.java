package com.example.bookmark.service;

import com.example.bookmark.data.BookmarkEntity;
import com.example.bookmark.data.BookmarkEntityRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@Transactional(readOnly = true)
public class BookmarkService {
    private final BookmarkEntityRepository bookmarkEntityRepository;

    public BookmarkService(BookmarkEntityRepository bookmarkEntityRepository) {
        this.bookmarkEntityRepository = bookmarkEntityRepository;
    }

    @Transactional
    public Bookmark create(Bookmark bookmark, UUID userIdentifier) {
        if (!userIdentifier.equals(bookmark.getUserIdentifier())) {
            throw new AccessDeniedException("Current user has no access to create bookmark for user with id " + bookmark.getUserIdentifier());
        }
        BookmarkEntity bookmarkEntity = bookmarkEntityRepository.save(toEntity(bookmark));
        return toBookmark(bookmarkEntity);
    }

    @Transactional
    public void deleteBookmarkEntityByIdentifier(UUID identifier, UUID userIdentifier) {
        bookmarkEntityRepository.deleteBookmarkEntityByIdentifierAndUserIdentifier(identifier, userIdentifier);
    }

    public List<Bookmark> findAllBookmarksByUser(UUID userIdentifier, User currentUser) {
        if (currentUser.isAdmin() || currentUser.getIdentifier().equals(userIdentifier)) {
            return bookmarkEntityRepository.findAllBookmarksByUserIdentifier(userIdentifier)
                    .stream()
                    .map(this::toBookmark)
                    .collect(Collectors.toList());
        } else {
            throw new AccessDeniedException("The current user is not allowed to access bookmarks for user " + userIdentifier);
        }
    }

    public Optional<Bookmark> findOneBookmarkByIdentifier(UUID identifier, UUID userIdentifier) {
        return bookmarkEntityRepository.findOneBookmarkByIdentifierAndUserIdentifier(identifier, userIdentifier).map(this::toBookmark);
    }

    public List<Bookmark> findAllBookmarksByCategory(String category, UUID userIdentifier) {
        return bookmarkEntityRepository.findAllBookmarksByCategoryAndUserIdentifier(category, userIdentifier)
                .stream()
                .map(this::toBookmark)
                .collect(Collectors.toList());
    }

    public List<Bookmark> search(String name, UUID userIdentifier) {
        return bookmarkEntityRepository.findAllBookmarksByNameAndUserIdentifier(name, userIdentifier)
                .stream()
                .map(this::toBookmark).collect(Collectors.toList());
    }

    private Bookmark toBookmark(BookmarkEntity b) {
        return new Bookmark(b.getIdentifier(), b.getName(), b.getDescription(),
                b.getCategory(), b.getUrl(), b.getUserIdentifier());
    }

    private BookmarkEntity toEntity(Bookmark bookmark) {
        return new BookmarkEntity(bookmark.getIdentifier(), bookmark.getName(),
                bookmark.getDescription(), bookmark.getCategory(), bookmark.getUrl(), bookmark.getUserIdentifier());
    }
}
