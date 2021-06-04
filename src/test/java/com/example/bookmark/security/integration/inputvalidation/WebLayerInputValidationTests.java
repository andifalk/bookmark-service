package com.example.bookmark.security.integration.inputvalidation;

import com.example.bookmark.api.BookmarkRestController;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.security.util.TestDataUtil;
import com.example.bookmark.security.util.WithMockBookmarkUser;
import com.example.bookmark.service.Bookmark;
import com.example.bookmark.service.BookmarkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.bookmark.security.util.TestDataUtil.USERID_BRUCE_WAYNE;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 4.1.1 Verify that the application enforces access control rules on a trusted service
 * layer, especially if client-side access control is present and could be bypassed.
 */
@IntegrationTest
@DisplayName("V5.1 Input Validation Requirements")
@WebMvcTest(BookmarkRestController.class)
public class WebLayerInputValidationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("unused")
    @MockBean
    private BookmarkService bookmarkService;

    @DisplayName("5.1.3 Verify that all input is validated using positive validation (allow lists)")
    @Nested
    class BookmarkInputValidationTests {

        @WithMockBookmarkUser
        @DisplayName("Verify valid url parameter is accepted")
        @ValueSource(strings = {"https://example.com", "http://example.com", "http://example.com/subpath", "http://example.com?a=test&b=test"})
        @ParameterizedTest
        void verifyValidUrlParameterShouldBeAllowed(String url) throws Exception {
            Bookmark bookmark = new Bookmark(UUID.randomUUID().toString(), "test", "test", "test", url, USERID_BRUCE_WAYNE);
            mvc.perform(post("/api/bookmarks").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(bookmark)))
                    .andExpect(status().isCreated());
        }

        @WithMockBookmarkUser
        @DisplayName("Verify invalid url parameter is denied")
        @NullAndEmptySource
        @ValueSource(strings = {"javascript:<iframe src=\"<alert>document.cookies</alert>\">", "data://somestuff",
                "ftp://example.com", "myserver", "http://example.com/../etc/passwd", "123"})
        @ParameterizedTest
        void verifyInvalidUrlParameterShouldBeDenied(String url) throws Exception {
            Bookmark bookmark = new Bookmark(UUID.randomUUID().toString(), "test", "test", "test", url, USERID_BRUCE_WAYNE);
            mvc.perform(post("/api/bookmarks").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(bookmark)))
                    .andExpect(status().isBadRequest());
        }

        @WithMockBookmarkUser
        @DisplayName("Verify invalid content type is denied")
        @ValueSource(strings = {TEXT_HTML_VALUE, APPLICATION_FORM_URLENCODED_VALUE, APPLICATION_PDF_VALUE, IMAGE_PNG_VALUE, "xyz"})
        @ParameterizedTest
        void verifyInvalidContentTypeShouldBeDenied(String contentType) throws Exception {
            Bookmark bookmark = new Bookmark(UUID.randomUUID().toString(), "test", "test", "test", "http://example.com", USERID_BRUCE_WAYNE);
            mvc.perform(post("/api/bookmarks").contentType(contentType).content(objectMapper.writeValueAsString(bookmark)))
                    .andExpect(status().isUnsupportedMediaType());
        }
    }
}
