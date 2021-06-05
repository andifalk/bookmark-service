package com.example.bookmark.security.util;

import com.example.bookmark.data.BookmarkEntity;
import com.example.bookmark.data.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public final class TestDataUtil {

    public static final String USERID_BRUCE_WAYNE = "c9caa4d1-5ad7-4dd1-8bd1-91b8bc5b9a48";
    public static final String USERID_BRUCE_BANNER = "938d2a36-9149-40af-be11-2f220cc87ca3";
    public static final String USERID_CLARK_KENT = "46502acd-7d7a-422a-9dc2-1dc5092f8c52";

    private static final String BOOKMARK_ID_SPRING = "f320a287-ffbd-4826-92e4-217cc6551f4f";
    private static final String BOOKMARK_ID_OWASP = "24c9b408-b7c5-42c1-b4dc-0462afd5eac2";
    private static final String BOOKMARK_ID_IETF = "8e055adf-cf38-426a-be35-0cb118fe8efa";
    private static final String BOOKMARK_ID_GOOGLE = "56c6f97f-1781-48b4-be85-80f27f180ed5";
    private static final String BOOKMARK_ID_AMAZON = "091878b2-e536-424b-a7b3-1671ac99c3e3";
    private static final String BOOKMARK_ID_EBAY = "9f392d8c-4b2b-408b-8da9-8dca6152a221";

    private TestDataUtil() {
    }

    public static List<UserEntity> createUsers() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return List.of(
                new UserEntity(UUID.fromString(USERID_CLARK_KENT), "Clark", "Kent",
                        passwordEncoder.encode("kent"), "clark.kent@example.com",
                        List.of("USER", "ADMIN")),
                new UserEntity(UUID.fromString(USERID_BRUCE_WAYNE), "Bruce", "Wayne",
                        passwordEncoder.encode("wayne"), "bruce.wayne@example.com",
                        List.of("USER")),
                new UserEntity(UUID.fromString(USERID_BRUCE_BANNER), "Bruce", "Banner",
                        passwordEncoder.encode("banner"), "bruce.banner@example.com",
                        List.of("USER"))
        );
    }

    public static List<BookmarkEntity> createBookmarks() throws MalformedURLException {
        return List.of(
                new BookmarkEntity(UUID.fromString(BOOKMARK_ID_AMAZON), "Amazon", "Amazon Shopping",
                        "Shopping", new URL("https://amazon.com"), UUID.fromString(USERID_BRUCE_WAYNE)),
                new BookmarkEntity(UUID.fromString(BOOKMARK_ID_EBAY), "Ebay", "Ebay Auctions",
                        "Shopping", new URL("https://ebay.com"), UUID.fromString(USERID_BRUCE_WAYNE)),
                new BookmarkEntity(UUID.fromString(BOOKMARK_ID_SPRING), "Spring", "Spring Framework",
                        "Development", new URL("https://spring.io"), UUID.fromString(USERID_BRUCE_BANNER)),
                new BookmarkEntity(UUID.fromString(BOOKMARK_ID_OWASP), "Owasp", "Open Web Application Security Project",
                        "Security", new URL("https://amazon.com"), UUID.fromString(USERID_BRUCE_BANNER)),
                new BookmarkEntity(UUID.fromString(BOOKMARK_ID_IETF), "IETF", "Internet Engineering Task Force",
                        "Security", new URL("https://www.ietf.org"), UUID.fromString(USERID_BRUCE_BANNER)),
                new BookmarkEntity(UUID.fromString(BOOKMARK_ID_GOOGLE), "Google", "Google Search",
                        "Search", new URL("https://google.com"), UUID.fromString(USERID_CLARK_KENT))
        );
    }

}
