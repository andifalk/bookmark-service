package com.example.bookmark.service;

import com.example.bookmark.data.BookmarkEntity;
import com.example.bookmark.data.BookmarkEntityRepository;
import com.example.bookmark.data.CustomBookmarkEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkEntityRepository bookmarkEntityRepository;
    private final CustomBookmarkEntityRepository customBookmarkEntityRepository;

    public BookmarkService(BookmarkEntityRepository bookmarkEntityRepository, CustomBookmarkEntityRepository customBookmarkEntityRepository) {
        this.bookmarkEntityRepository = bookmarkEntityRepository;
        this.customBookmarkEntityRepository = customBookmarkEntityRepository;
    }

    @Transactional
    public Bookmark create(Bookmark bookmark) {
        BookmarkEntity bookmarkEntity = bookmarkEntityRepository.save(toEntity(bookmark));
        return toBookmark(bookmarkEntity);
    }

    @Transactional
    public int deleteBookmarkEntityByIdentifier(String identifier) {
        return bookmarkEntityRepository.deleteBookmarkEntityByIdentifier(identifier);
    }

    public List<Bookmark> findAllBookmarksByUser(String userIdentifier) {
        return customBookmarkEntityRepository.findAllBookmarksByUser(userIdentifier)
                .stream()
                .map(this::toBookmark)
                .collect(Collectors.toList());
    }

    public Optional<Bookmark> findOneBookmarkByIdentifier(String identifier) {
        return customBookmarkEntityRepository.findOneBookmarkByIdentifier(identifier).map(this::toBookmark);
    }

    public List<Bookmark> findAllBookmarksByCategory(String category) {
        return customBookmarkEntityRepository.findAllBookmarksByCategory(category)
                .stream()
                .map(this::toBookmark)
                .collect(Collectors.toList());
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
