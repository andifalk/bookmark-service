package com.example.bookmark.security.integration.injection;

import com.example.bookmark.data.BookmarkEntityRepository;
import com.example.bookmark.data.UserEntity;
import com.example.bookmark.data.UserEntityRepository;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.security.util.TestDataUtil;
import com.example.bookmark.service.BookmarkService;
import com.example.bookmark.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.servlet.Filter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
@DisplayName("5.3.4 Verify that database queries use parameterized queries or are otherwise protected" +
        "from database injection attacks")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class SqlInjectionPreventionTests {

    private static final String SQL_INJECTION_PAYLOAD = "invalid' or 1=1--";

    @MockBean(name = "springSecurityFilterChain")
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
    void initTestData() {
        List<UserEntity> userEntityList = TestDataUtil.createUsers().stream().map(u -> userEntityRepository.save(u)).collect(Collectors.toList());
        TestDataUtil.createBookmarks().stream().map(b -> bookmarkEntityRepository.save(b)).collect(Collectors.toList());
    }

    @AfterEach
    void cleanupTestData() {
        bookmarkEntityRepository.deleteAll();
        userEntityRepository.deleteAll();
    }

    @Test
    void verifyChangePassword() {
        assertThatThrownBy(() -> userService.changePassword(SQL_INJECTION_PAYLOAD, "oldPassword", "newPassword")).hasMessage("No user found for identifier [invalid' or 1=1--]");
    }

    @Test
    void verifyFindUserByIdentifier() {
        assertThat(userService.findByIdentifier(SQL_INJECTION_PAYLOAD)).isNotPresent();
    }

    @Test
    void verifyFindUserByEmail() {
        assertThat(userService.findUserByEmail(SQL_INJECTION_PAYLOAD)).isNotPresent();
    }

    @Test
    void verifySearchBookmarks() {
        assertThat(bookmarkService.search(SQL_INJECTION_PAYLOAD)).isEmpty();
    }

    @Test
    void verifyFindAllBookmarksByUser() {
        assertThat(bookmarkService.findAllBookmarksByUser(SQL_INJECTION_PAYLOAD)).isEmpty();
    }

    @Test
    void verifyFindAllBookmarksByCategory() {
        assertThat(bookmarkService.findAllBookmarksByCategory(SQL_INJECTION_PAYLOAD)).isEmpty();
    }

    @Test
    void verifyFindBookmarkByIdentifier() {
        assertThat(bookmarkService.findOneBookmarkByIdentifier(SQL_INJECTION_PAYLOAD)).isNotPresent();
    }

}
