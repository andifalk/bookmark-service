package com.example.bookmark.service;

import com.example.bookmark.data.BookmarkEntity;
import com.example.bookmark.data.BookmarkEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookmarkService {
    private final BookmarkEntityRepository bookmarkEntityRepository;

    public BookmarkService(BookmarkEntityRepository bookmarkEntityRepository) {
        this.bookmarkEntityRepository = bookmarkEntityRepository;
    }

    @Transactional
    public Bookmark create(Bookmark bookmark) {
        BookmarkEntity bookmarkEntity = bookmarkEntityRepository.save(toEntity(bookmark));
        return toBookmark(bookmarkEntity);
    }

    @Transactional
    public void deleteBookmarkEntityByIdentifier(UUID identifier) {
        bookmarkEntityRepository.deleteBookmarkEntityByIdentifier(identifier);
    }

    public List<Bookmark> findAllBookmarksByUser(UUID userIdentifier) {
        return bookmarkEntityRepository.findAllBookmarksByUserIdentifier(userIdentifier)
                .stream()
                .map(this::toBookmark)
                .collect(Collectors.toList());
    }

    public Optional<Bookmark> findOneBookmarkByIdentifier(UUID identifier) {
        return bookmarkEntityRepository.findOneBookmarkByIdentifier(identifier).map(this::toBookmark);
    }

    public List<Bookmark> findAllBookmarksByCategory(String category) {
        return bookmarkEntityRepository.findAllBookmarksByCategory(category)
                .stream()
                .map(this::toBookmark)
                .collect(Collectors.toList());
    }

    public List<Bookmark> search(String name) {
        return bookmarkEntityRepository.findAllBookmarksByName(name)
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
