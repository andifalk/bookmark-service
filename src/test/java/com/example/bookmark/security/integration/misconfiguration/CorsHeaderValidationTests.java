package com.example.bookmark.security.integration.misconfiguration;

import com.example.bookmark.data.UserEntityRepository;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.security.util.TestDataUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 14.5.3 Verify that the Cross-Origin Resource Sharing (CORS) Access-Control-AllowOrigin header uses a
 * strict allow list of trusted domains and subdomains to
 * match against and does not support the "null" origin.
 */
@IntegrationTest
@WithMockUser
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("14.5.3 Verify that the CORS Access-Control-AllowOrigin header uses a strict allow list")
public class CorsHeaderValidationTests {

    private static final String VALID_ORIGIN = "http://localhost:3000";
    private static final String INVALID_ORIGIN = "https://evil.com";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserEntityRepository userEntityRepository;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void initTestData() {
        TestDataUtil.createUsers().forEach(u -> userEntityRepository.save(u));
    }

    @DisplayName("GET request is not allowed for invalid origin")
    @Test
    public void verifyGetCorsRequestNotAllowed() throws Exception {
        mvc.perform(get("/api/users")
                .contentType(APPLICATION_JSON)
                .header("Origin", INVALID_ORIGIN))
                .andExpect(status().isForbidden()).andDo(print());
    }

    @DisplayName("GET request is allowed for valid origin")
    @Test
    public void verifyGetCorsRequestIsAllowed() throws Exception {
        mvc.perform(get("/api/users")
                .contentType(APPLICATION_JSON)
                .header("Origin", VALID_ORIGIN))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", VALID_ORIGIN))
                .andDo(print());
    }

    @DisplayName("Preflight request disallows PATCH requests")
    @Test
    public void verifyPreflightPatchCorsRequestIsNotAllowed() throws Exception {
        mvc.perform(options("/api/users")
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PATCH")
                .header("Origin", VALID_ORIGIN))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"))
                .andDo(print());
    }

    @DisplayName("14.5.3 Verify that the CORS Access-Control-AllowOrigin header does not support the \"null\" origin.")
    @Test
    public void verifyPreflightNullOriginCorsRequestIsNotAllowed() throws Exception {
        mvc.perform(options("/api/users")
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                .header("Origin", ""))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"))
                .andDo(print());
    }

    @DisplayName("Preflight request allows GET,POST,PUT,DELETE requests")
    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE"})
    public void verifyPreflightCorsRequestIsAllowed(String method) throws Exception {
        mvc.perform(options("/api/users")
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, method)
                .header("Origin", VALID_ORIGIN))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", VALID_ORIGIN))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"))
                .andDo(print());
    }

    @DisplayName("Preflight request disallows GET,POST,PUT,DELETE requests for invalid origin")
    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE"})
    public void verifyPreflightCorsRequestIsNotAllowed(String method) throws Exception {
        mvc.perform(options("/api/users")
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, method)
                .header("Origin", INVALID_ORIGIN))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Methods"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Credentials"))
                .andDo(print());
    }

    @AfterEach
    void cleanupTestData() {
        userEntityRepository.deleteAll();
    }
}
