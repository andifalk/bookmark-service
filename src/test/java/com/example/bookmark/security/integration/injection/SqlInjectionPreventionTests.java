package com.example.bookmark.security.integration.injection;

import com.example.bookmark.data.BookmarkEntityRepository;
import com.example.bookmark.data.UserEntityRepository;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.security.util.TestDataUtil;
import com.example.bookmark.service.BookmarkService;
import com.example.bookmark.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import jakarta.servlet.Filter;
import java.net.MalformedURLException;
import java.util.UUID;

import static com.example.bookmark.security.util.TestDataUtil.USERID_BRUCE_WAYNE;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@WithMockUser
@IntegrationTest
@DisplayName("5.3.4 Verify that database queries use parameterized queries or are otherwise protected" +
        "from database injection attacks")
@SpringBootTest(webEnvironment = NONE)
class SqlInjectionPreventionTests {

    private static final String SQL_INJECTION_PAYLOAD = "invalid' or 1=1--";

    @MockBean(name = "securityFilterChain")
    Filter springSecurityFilterChain;

    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private BookmarkEntityRepository bookmarkEntityRepository;

    @BeforeEach
    void initTestData() throws MalformedURLException {
        TestDataUtil.createUsers().forEach(u -> userEntityRepository.save(u));
        TestDataUtil.createBookmarks().forEach(b -> bookmarkEntityRepository.save(b));
    }

    @Test
    void verifyChangePassword() {
        assertThatThrownBy(() -> userService.changePassword(UUID.randomUUID(), SQL_INJECTION_PAYLOAD, "newPassword")).hasMessage("User and/or password is not valid");
    }

    @Test
    void verifyFindUserByIdentifier() {
        assertThat(userService.findByIdentifier(UUID.randomUUID())).isNotPresent();
    }

    @Test
    void verifySearchBookmarks() {
        assertThat(bookmarkService.search(SQL_INJECTION_PAYLOAD, UUID.fromString(USERID_BRUCE_WAYNE))).isEmpty();
    }

    @Test
    void verifyFindAllBookmarksByCategory() {
        assertThat(bookmarkService.findAllBookmarksByCategory(SQL_INJECTION_PAYLOAD, UUID.fromString(USERID_BRUCE_WAYNE))).isEmpty();
    }

    // Not testable any more due to strong UUID type
    /*
    @Test
    void verifyFindBookmarkByIdentifier() {
        assertThatNoException().isThrownBy(() -> bookmarkService.findOneBookmarkByIdentifier(SQL_INJECTION_PAYLOAD));
        assertThat(bookmarkService.findOneBookmarkByIdentifier(SQL_INJECTION_PAYLOAD)).isNotPresent();
    }*/

    @AfterEach
    void cleanupTestData() {
        bookmarkEntityRepository.deleteAll();
        userEntityRepository.deleteAll();
    }
}
