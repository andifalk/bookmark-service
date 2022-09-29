package com.example.bookmark.security.integration.fileupload;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static com.example.bookmark.security.util.TestDataUtil.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * V12: File and Resources Verification Requirements.
 */
@IntegrationTest
@DisplayName("V12: File and Resources Verification Requirements")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SecureFileUploadTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @DisplayName("V12.1 File Upload Requirements")
    @Nested
    class FileUploadTests {

        @WithMockBookmarkUser
        @DisplayName("Bruce Wayne can upload new bookmarks")
        @Test
        void verifyBookmarksCanBeAccessed() throws Exception {
            ClassPathResource resource = new ClassPathResource("upload/bookmarks.xlsx");
            mvc.perform(multipart("/api/bookmarks/upload")
                    .file(new MockMultipartFile(
                            "file",
                            "bookmarks.xlsx",
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            resource.getInputStream()
                    )).contentType(MULTIPART_FORM_DATA).with(csrf()))
                    .andExpect(status().isOk()).andDo(print());
        }

        @WithMockBookmarkUser
        @DisplayName("12.1.1 Verify that the application will not accept large files...")
        @Test
        void verifyLargeFilesAreNotAccepted() throws Exception {
            ClassPathResource resource = new ClassPathResource("upload/big.pdf");
            mvc.perform(multipart("/api/bookmarks/upload")
                    .file(new MockMultipartFile(
                            "file",
                            "big.pdf",
                            "application/pdf",
                            resource.getInputStream()
                    )).contentType(MULTIPART_FORM_DATA).with(csrf()))
                    .andExpect(status().isBadRequest()).andExpect(status().reason(not(startsWith("No valid entries or contents found")))).andDo(print());
        }
    }

    @DisplayName("V12.2 File Integrity Requirements")
    @Nested
    class FileIntegrityRequirements {

        @WithMockBookmarkUser
        @DisplayName("12.2.1 Verify that files obtained from untrusted sources are validated to be of expected type based on the file's content")
        @Test
        void verifyInvalidFileTypeUpload() throws Exception {
            ClassPathResource resource = new ClassPathResource("upload/invalid_type.xlsx");
            mvc.perform(multipart("/api/bookmarks/upload")
                    .file(new MockMultipartFile(
                            "file",
                            "bookmarks.xlsx",
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            resource.getInputStream()
                    )).contentType(MULTIPART_FORM_DATA).with(csrf()))
                    .andExpect(status().isBadRequest()).andExpect(status().reason(not(startsWith("No valid entries or contents found")))).andDo(print());
        }

    }

}
