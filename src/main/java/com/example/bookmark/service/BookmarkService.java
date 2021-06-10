package com.example.bookmark.service;

import com.example.bookmark.data.BookmarkEntity;
import com.example.bookmark.data.BookmarkEntityRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.tika.config.TikaConfig;

@Service
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@Transactional(readOnly = true)
public class BookmarkService {
    private final BookmarkEntityRepository bookmarkEntityRepository;
    private final Detector detector = TikaConfig.getDefaultConfig().getDetector();

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

    @Transactional
    public List<Bookmark> importBookmarks(MultipartFile file, User user) {
        TikaInputStream tikaInputStream = null;
        try {
            tikaInputStream = TikaInputStream.get(file.getInputStream());
            MediaType baseType = detector.detect(tikaInputStream, new Metadata()).getBaseType();
            if (!baseType.toString().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                throw new IllegalArgumentException("Wrong file type");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error handling upload file: " + ex.getMessage());
        }
        List<Bookmark> bookmarks = new ArrayList<>();
        try (InputStream in = tikaInputStream) {
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
                bookmark.setIdentifier(UUID.randomUUID());
                bookmark.setName(row.getCell(0).getStringCellValue());
                bookmark.setDescription(row.getCell(1).getStringCellValue());
                bookmark.setCategory(row.getCell(2).getStringCellValue());
                bookmark.setUrl(new URL(row.getCell(3).getStringCellValue()));
                bookmark.setUserIdentifier(user.getIdentifier());
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
