package com.example.bookmark.security.integration.brokenaccesscontrol;

import com.example.bookmark.data.BookmarkEntityRepository;
import com.example.bookmark.data.UserEntityRepository;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.security.util.TestDataUtil;
import com.example.bookmark.security.util.WithMockBookmarkUser;
import com.example.bookmark.service.BookmarkService;
import com.example.bookmark.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import jakarta.servlet.Filter;

import static com.example.bookmark.security.util.TestDataUtil.USERID_BRUCE_BANNER;
import static com.example.bookmark.security.util.TestDataUtil.USERID_BRUCE_WAYNE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

/**
 * 4.1.1 Verify that the application enforces access control rules on a trusted service
 * layer, especially if client-side access control is present and could be bypassed.
 */
@IntegrationTest
@DisplayName("V4: Access Control Verification Requirements (Service Layer)")
@SpringBootTest(webEnvironment = NONE)
public class ServiceLayerAuthorizationTests {

    @MockBean(name = "securityFilterChain")
    Filter springSecurityFilterChain;

    @Autowired
    private UserService userService;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BookmarkEntityRepository bookmarkEntityRepository;

    @BeforeEach
    void initTestData() {
        TestDataUtil.createUsers().forEach(u -> userEntityRepository.save(u));
        TestDataUtil.createBookmarks().forEach(b -> bookmarkEntityRepository.save(b));
    }

    @DisplayName("4.1.1 Verify that the application enforces access control rules")
    @Nested
    class RoleAuthorizationTests {

        @WithMockUser(roles = "ADMIN")
        @DisplayName("List of users can be accessed by administrative user")
        @Test
        void verifyFindAllUsersCanBeAccessed() {
            assertThat(userService.findAll()).isNotEmpty();
        }

        @WithMockUser(roles = "USER")
        @DisplayName("List of users cannot be accessed by standard user")
        @Test
        void verifyFindAllUsersIsForbidden() {
            assertThatThrownBy(() -> userService.findAll()).isInstanceOf(AccessDeniedException.class);
        }
    }

    @DisplayName("4.2.1 Verify that APIs are protected against Insecure Direct Object Reference attacks")
    @Nested
    class ObjectAuthorizationTests {

        @WithMockBookmarkUser
        @DisplayName("Bruce Wayne can access his bookmarks")
        @Test
        void verifyBruceWayneCanAccessHisBookmarks() {
            assertThat(bookmarkService.findAllBookmarksByUser(USERID_BRUCE_WAYNE)).isNotEmpty();
        }

        @WithMockBookmarkUser(identifier = USERID_BRUCE_BANNER)
        @DisplayName("Bruce Banner cannot access bookmarks of Bruce Wayne")
        @Test
        void verifyBruceBannerCannotAccessBookmarksOfBruceWayne() {
            assertThatThrownBy(() -> bookmarkService.findAllBookmarksByUser(USERID_BRUCE_WAYNE))
                    .isInstanceOf(AccessDeniedException.class);
        }
    }

    @AfterEach
    void cleanupTestData() {
        bookmarkEntityRepository.deleteAll();
        userEntityRepository.deleteAll();
    }
}
