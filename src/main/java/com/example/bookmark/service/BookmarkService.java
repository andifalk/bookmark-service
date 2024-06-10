package com.example.bookmark.service;

import com.example.bookmark.data.BookmarkEntity;
import com.example.bookmark.data.BookmarkEntityRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.bookmark.data.CustomBookmarkEntityRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkEntityRepository bookmarkEntityRepository;
    private final CustomBookmarkEntityRepository customBookmarkEntityRepository;
    private final EntityManager entityManager;

    public BookmarkService(BookmarkEntityRepository bookmarkEntityRepository, CustomBookmarkEntityRepository customBookmarkEntityRepository, EntityManager entityManager) {
        this.bookmarkEntityRepository = bookmarkEntityRepository;
        this.customBookmarkEntityRepository = customBookmarkEntityRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Bookmark create(Bookmark bookmark) {
        BookmarkEntity bookmarkEntity = bookmarkEntityRepository.save(toEntity(bookmark));
        return toBookmark(bookmarkEntity);
    }

    @Transactional
    public void deleteBookmarkEntityByIdentifier(String identifier) {
        bookmarkEntityRepository.deleteBookmarkEntityByIdentifier(identifier);
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

    public List<Bookmark> search(String name) {
        final Session session = entityManager.unwrap(Session.class);
        return session.doReturningWork(connection -> {
            List<Bookmark> bookmarks = new ArrayList<>();
            try (Statement st = connection
                    .createStatement()) {
                try (ResultSet rs = st.executeQuery(
                        "select * from bookmark_entity where name like '%" + name + "%'"
                )) {
                    while (rs.next()) {
                        bookmarks.add(new Bookmark(
                                rs.getString("identifier"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getString("category"), rs.getString("url"), rs.getString("user_identifier")));
                    }
                }
                return bookmarks;
            }
        });
    }

    @Transactional
    public List<Bookmark> importBookmarks(MultipartFile file) {
        List<Bookmark> bookmarks = new ArrayList<>();
        try (InputStream in = file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Row row;
            Iterator<Row> rowIterator = sheet.rowIterator();
            Bookmark bookmark;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    continue;
                }
                bookmark = new Bookmark();
                bookmark.setIdentifier(UUID.randomUUID().toString());
                bookmark.setName(row.getCell(0).getStringCellValue());
                bookmark.setDescription(row.getCell(1).getStringCellValue());
                bookmark.setCategory(row.getCell(2).getStringCellValue());
                bookmark.setUrl(row.getCell(3).getStringCellValue());
                bookmark.setUserIdentifier(row.getCell(4).getStringCellValue());
                bookmarks.add(bookmark);
            }
            return bookmarks.stream()
                    .map(this::toEntity)
                    .map(bookmarkEntityRepository::save)
                    .map(this::toBookmark)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
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
