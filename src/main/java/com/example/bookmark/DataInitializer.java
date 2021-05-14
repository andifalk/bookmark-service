package com.example.bookmark;

import com.example.bookmark.data.BookmarkEntity;
import com.example.bookmark.data.BookmarkEntityRepository;
import com.example.bookmark.data.UserEntity;
import com.example.bookmark.data.UserEntityRepository;
import com.example.bookmark.service.Bookmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {

    public static final String USERID_BRUCE_WAYNE = "c9caa4d1-5ad7-4dd1-8bd1-91b8bc5b9a48";
    private static final String USERID_BRUCE_BANNER = "938d2a36-9149-40af-be11-2f220cc87ca3";
    private static final String USERID_CLARK_KENT = "46502acd-7d7a-422a-9dc2-1dc5092f8c52";

    private static final String BOOKMARK_ID_SPRING = "f320a287-ffbd-4826-92e4-217cc6551f4f";
    private static final String BOOKMARK_ID_OWASP = "24c9b408-b7c5-42c1-b4dc-0462afd5eac2";
    private static final String BOOKMARK_ID_IETF = "8e055adf-cf38-426a-be35-0cb118fe8efa";
    private static final String BOOKMARK_ID_GOOGLE = "56c6f97f-1781-48b4-be85-80f27f180ed5";
    private static final String BOOKMARK_ID_AMAZON = "091878b2-e536-424b-a7b3-1671ac99c3e3";
    private static final String BOOKMARK_ID_EBAY = "9f392d8c-4b2b-408b-8da9-8dca6152a221";

    private final UserEntityRepository userEntityRepository;
    private final BookmarkEntityRepository bookmarkEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserEntityRepository userEntityRepository, BookmarkEntityRepository bookmarkEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.bookmarkEntityRepository = bookmarkEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        Logger log = LoggerFactory.getLogger(this.getClass().getName());
        createUsers(log);
        createBookmarks(log);
    }

    private void createUsers(Logger log) {
        log.info("Creating some users...");
        List<UserEntity> userEntities = Stream.of(
                new UserEntity(USERID_CLARK_KENT, "Clark", "Kent",
                        passwordEncoder.encode("kent"), "clark.kent@example.com",
                        List.of("USER", "ADMIN")),
                new UserEntity(USERID_BRUCE_WAYNE, "Bruce", "Wayne",
                        passwordEncoder.encode("wayne"), "bruce.wayne@example.com",
                        List.of("USER")),
                new UserEntity(USERID_BRUCE_BANNER, "Bruce", "Banner",
                        passwordEncoder.encode("banner"), "bruce.banner@example.com",
                        List.of("USER"))
        ).map(userEntityRepository::save).collect(Collectors.toList());
        log.info("Successfully created {} users", userEntities.size());
    }

    private void createBookmarks(Logger log) {
        log.info("Creating some bookmarks...");
        List<BookmarkEntity> bookmarkEntities = Stream.of(
                new BookmarkEntity(BOOKMARK_ID_AMAZON, "Amazon", "Amazon Shopping",
                        "Shopping", "https://amazon.com", USERID_BRUCE_WAYNE),
                new BookmarkEntity(BOOKMARK_ID_EBAY, "Ebay", "Ebay Auctions",
                        "Shopping", "https://ebay.com", USERID_BRUCE_WAYNE),
                new BookmarkEntity(BOOKMARK_ID_SPRING, "Spring", "Spring Framework",
                        "Development", "https://spring.io", USERID_BRUCE_BANNER),
                new BookmarkEntity(BOOKMARK_ID_OWASP, "Owasp", "Open Web Application Security Project",
                        "Security", "https://amazon.com", USERID_BRUCE_BANNER),
                new BookmarkEntity(BOOKMARK_ID_IETF, "IETF", "Internet Engineering Task Force",
                        "Security", "https://www.ietf.org", USERID_BRUCE_BANNER),
                new BookmarkEntity(BOOKMARK_ID_GOOGLE, "Google", "Google Search",
                        "Search", "https://google.com", USERID_CLARK_KENT)
        ).map(bookmarkEntityRepository::save).collect(Collectors.toList());
        log.info("Successfully created {} bookmarks", bookmarkEntities.size());
    }

}
