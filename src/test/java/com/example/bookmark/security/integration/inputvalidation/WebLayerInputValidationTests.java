package com.example.bookmark.security.integration.inputvalidation;

import com.example.bookmark.api.BookmarkRestController;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.security.util.WithMockBookmarkUser;
import com.example.bookmark.service.BookmarkService;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.bookmark.security.util.TestDataUtil.USERID_BRUCE_WAYNE;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @SuppressWarnings("unused")
    @MockBean
    private BookmarkService bookmarkService;

    @DisplayName("5.1.3 Verify that all input is validated using positive validation (allow lists)")
    @Nested
    class BookmarkInputValidationTests {

        @WithMockBookmarkUser
        @DisplayName("Verify valid url parameter is accepted")
        @ValueSource(strings = {"https://example.com", "http://example.com", "ftp://example.com", "file:///foo/bar.txt",
                "http://example.com/subpath", "http://example.com?a=test&b=test"})
        @ParameterizedTest
        void verifyValidUrlParameterShouldBeAllowed(String url) throws Exception {
            String bookmarkRequestObject = createBookmarkRequestObject(UUID.randomUUID().toString(), "valid", "Valid bookmark", "valid", url);
            mvc.perform(post("/api/bookmarks").with(csrf()).contentType(APPLICATION_JSON).content(bookmarkRequestObject))
                    .andExpect(status().isCreated()).andDo(print());
        }

        @WithMockBookmarkUser
        @DisplayName("Verify invalid url parameter is denied")
        @NullAndEmptySource
        @ValueSource(strings = {"javascript:<iframe src=\"<alert>document.cookies</alert>\">", "data://somestuff",
                "jar://example.com", "git://example.com", "myserver", "http://example.com/../etc/passwd", "123"})
        @ParameterizedTest
        void verifyInvalidUrlParameterShouldBeDenied(String url) throws Exception {
            String bookmarkRequestObject = createBookmarkRequestObject(UUID.randomUUID().toString(), "invalid", "Invalid bookmark", "invalid", url);
            mvc.perform(post("/api/bookmarks").with(csrf()).contentType(APPLICATION_JSON).content(bookmarkRequestObject))
                    .andExpect(status().isBadRequest()).andDo(print());
        }

        @WithMockBookmarkUser
        @DisplayName("Verify invalid content type is denied")
        @ValueSource(strings = {TEXT_HTML_VALUE, APPLICATION_FORM_URLENCODED_VALUE, APPLICATION_PDF_VALUE, IMAGE_PNG_VALUE, "xyz"})
        @ParameterizedTest
        void verifyInvalidContentTypeShouldBeDenied(String contentType) throws Exception {
            String bookmarkRequestObject = createBookmarkRequestObject(UUID.randomUUID().toString(), "valid", "Valid bookmark", "valie", "https://example.com");
            mvc.perform(post("/api/bookmarks").with(csrf()).contentType(contentType).content(bookmarkRequestObject))
                    .andExpect(status().isUnsupportedMediaType()).andDo(print());
        }
    }

    private String createBookmarkRequestObject(String identifier, String name, String description, String category, String url) {
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put("identifier", identifier);
        valuesMap.put("name", name);
        valuesMap.put("description", description);
        valuesMap.put("category", category);
        valuesMap.put("url", url);
        valuesMap.put("userIdentifier", USERID_BRUCE_WAYNE);
        return new JSONObject(valuesMap).toString();
    }
}
