package com.example.bookmark.security.integration.brokenaccesscontrol;

import com.example.bookmark.data.BookmarkEntityRepository;
import com.example.bookmark.data.UserEntityRepository;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.security.util.TestDataUtil;
import com.example.bookmark.security.util.WithMockBookmarkUser;
import com.example.bookmark.service.Bookmark;
import com.example.bookmark.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static com.example.bookmark.security.util.TestDataUtil.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 4.1.1 Verify that the application enforces access control rules on a trusted service
 * layer, especially if client-side access control is present and could be bypassed.
 */
@IntegrationTest
@DisplayName("4.2.1 Verify that APIs are protected against Insecure Direct Object Reference attacks")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WebLayerObjectAuthorizationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("unused")
    @MockBean
    private UserService userService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BookmarkEntityRepository bookmarkEntityRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void initTestData() throws MalformedURLException {
        TestDataUtil.createUsers().forEach(u -> userEntityRepository.save(u));
        TestDataUtil.createBookmarks().forEach(u -> bookmarkEntityRepository.save(u));
    }

    @AfterEach
    void cleanTestData() {
        bookmarkEntityRepository.deleteAll();
        userEntityRepository.deleteAll();
    }

    @DisplayName("Verify getting bookmarks is secure")
    @Nested
    class GetBookmarksAuthorizationTests {

        @WithMockBookmarkUser
        @DisplayName("Bruce Wayne can access his own bookmarks")
        @Test
        void verifyBookmarksCanBeAccessed() throws Exception {
            mvc.perform(get("/api/bookmarks?userid=" + USERID_BRUCE_WAYNE).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andDo(print());
        }

        @WithMockBookmarkUser(identifier = USERID_BRUCE_BANNER)
        @DisplayName("Bruce Banner cannot access bookmarks of Bruce Wayne")
        @Test
        void verifyAccessingOthersBookmarksIsForbidden() throws Exception {
            mvc.perform(get("/api/bookmarks?userid=" + USERID_BRUCE_WAYNE).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden()).andDo(print());
        }

        @WithMockBookmarkUser(identifier = USERID_CLARK_KENT, roles = {"ADMIN"})
        @DisplayName("Clark Kent as administrative user can access bookmarks of Bruce Wayne")
        @Test
        void verifyOthersBookmarksCanBeAccessedByAdmin() throws Exception {
            mvc.perform(get("/api/bookmarks?userid=" + USERID_BRUCE_WAYNE).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andDo(print());
        }
    }

    @DisplayName("Verify creating my onw bookmarks is secure")
    @Nested
    class CreateBookmarkAuthorizationTests {

        @WithMockBookmarkUser
        @DisplayName("Bruce Wayne can add a new bookmark to his own ones")
        @Test
        void verifyBookmarksCanBeAccessed() throws Exception {
            Bookmark bookmark = new Bookmark(UUID.randomUUID(), "Test", "Test Bookmark", "Test",
                    new URL("https://example.com"), UUID.fromString(USERID_BRUCE_WAYNE));
            mvc.perform(post("/api/bookmarks").with(csrf())
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookmark)))
                    .andExpect(status().isCreated()).andDo(print());
        }

        @WithMockBookmarkUser(identifier = USERID_BRUCE_BANNER)
        @DisplayName("Bruce Banner cannot create a bookmark for Bruce Wayne")
        @Test
        void verifyAccessingOthersBookmarksIsForbidden() throws Exception {
            Bookmark bookmark = new Bookmark(UUID.randomUUID(), "Test", "Test Bookmark", "Test",
                    new URL("https://example.com"), UUID.fromString(USERID_BRUCE_WAYNE));
            mvc.perform(post("/api/bookmarks").with(csrf())
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookmark)))
                    .andExpect(status().isForbidden()).andDo(print());
        }

        @WithMockBookmarkUser(identifier = USERID_CLARK_KENT, roles = {"ADMIN"})
        @DisplayName("Clark Kent as administrative user cannot create a bookmark for Bruce Wayne")
        @Test
        void verifyOthersBookmarksCanBeAccessedByAdmin() throws Exception {
            Bookmark bookmark = new Bookmark(UUID.randomUUID(), "Test", "Test Bookmark", "Test",
                    new URL("https://example.com"), UUID.fromString(USERID_BRUCE_WAYNE));
            mvc.perform(post("/api/bookmarks").with(csrf())
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookmark)))
                    .andExpect(status().isForbidden()).andDo(print());
        }
    }

}
