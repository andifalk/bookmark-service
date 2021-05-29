package com.example.bookmark.security.integration.brokenaccesscontrol;

import com.example.bookmark.api.UserRestController;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.service.User;
import com.example.bookmark.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 4.1.1 Verify that the application enforces access control rules on a trusted service
 * layer, especially if client-side access control is present and could be bypassed.
 */
@IntegrationTest
@DisplayName("V4: Access Control Verification Requirements (Web Layer)")
@WebMvcTest(UserRestController.class)
public class WebLayerAuthorizationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @DisplayName("4.1.1 Verify that the application enforces access control rules")
    @Nested
    class AuthorizationTests {

        @WithMockUser(roles = "ADMIN")
        @DisplayName("List of users can be accessed be administrative user")
        @Test
        void verifyFindAllUsersCanBeAccessed() throws Exception {
            mvc.perform(get("/api/users?userid=12345").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @WithMockUser(roles = "USER")
        @DisplayName("List of users cannot be accessed be standard user")
        @Test
        void verifyFindAllUsersIsForbidden() throws Exception {
            mvc.perform(get("/api/users?userid=12345").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

    }

    @DisplayName("4.2.2 Verify that the application enforces a strong anti-CSRF mechanism")
    @Nested
    class CsrfAuthorizationTests {

        /*
         * 4.2.2 Verify that the application or framework enforces a strong anti-CSRF mechanism
         * to protect authenticated functionality, and effective anti-automation or
         * antiCSRF protects unauthenticated functionality
         */

        @DisplayName("User cannot be created without valid CSRF token")
        @Test
        void verifyCreateUserIsForbiddenWithoutCsrfToken() throws Exception {
            User user = new User(UUID.randomUUID().toString(), "Max", "Test", "12345", "test@example.com", List.of("USER"));
            mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isForbidden());
        }

        @DisplayName("User can be created with valid CSRF token")
        @Test
        void verifyCreateUserIsSuccessfulWithCsrfToken() throws Exception {
            User user = new User(UUID.randomUUID().toString(), "Max", "Test", "12345", "test@example.com", List.of("USER"));
            mvc.perform(post("/api/users").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isCreated());
        }

    }
}
