package com.example.bookmark.security.integration.brokenaccesscontrol;

import com.example.bookmark.data.BookmarkEntityRepository;
import com.example.bookmark.data.UserEntity;
import com.example.bookmark.data.UserEntityRepository;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.security.util.TestDataUtil;
import com.example.bookmark.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import javax.servlet.Filter;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 4.1.1 Verify that the application enforces access control rules on a trusted service
 * layer, especially if client-side access control is present and could be bypassed.
 */
@IntegrationTest
@DisplayName("V4: Access Control Verification Requirements (Service Layer)")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ServiceLayerAuthorizationTests {

    @Autowired
    private UserService userService;

    @MockBean(name = "springSecurityFilterChain")
    Filter springSecurityFilterChain;

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private BookmarkEntityRepository bookmarkEntityRepository;

    @BeforeEach
    void initTestData() {
        TestDataUtil.createUsers().forEach(u -> userEntityRepository.save(u));
        TestDataUtil.createBookmarks().forEach(b -> bookmarkEntityRepository.save(b));
    }

    @AfterEach
    void cleanupTestData() {
        bookmarkEntityRepository.deleteAll();
        userEntityRepository.deleteAll();
    }

    @DisplayName("4.1.1 Verify that the application enforces access control rules")
    @Nested
    class AuthorizationTests {

        @WithMockUser(roles = "ADMIN")
        @DisplayName("List of users can be accessed be administrative user")
        @Test
        void verifyFindAllUsersCanBeAccessed() {
            assertThat(userService.findAll()).isNotEmpty();
        }

        @WithMockUser(roles = "USER")
        @DisplayName("List of users cannot be accessed be standard user")
        @Test
        void verifyFindAllUsersIsForbidden() {
            assertThatThrownBy(() -> userService.findAll()).isInstanceOf(AccessDeniedException.class);
        }

    }

}
