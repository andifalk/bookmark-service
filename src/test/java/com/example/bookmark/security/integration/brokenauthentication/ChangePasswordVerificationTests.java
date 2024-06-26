package com.example.bookmark.security.integration.brokenauthentication;

import com.example.bookmark.data.UserEntityRepository;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.service.User;
import com.example.bookmark.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.Filter;
import java.util.Optional;

import static com.example.bookmark.security.util.TestDataUtil.USERID_BRUCE_WAYNE;
import static com.example.bookmark.security.util.TestDataUtil.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@IntegrationTest
@DisplayName("V2.1 Password Security Requirements")
@SpringBootTest(webEnvironment = NONE)
class ChangePasswordVerificationTests {

    @MockBean(name = "securityFilterChain")
    Filter apiSecurityFilterChain;

    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void initTestData() {
        createUsers().forEach(u -> userEntityRepository.save(u));
    }

    @DisplayName("2.1.5 Verify users can change their password")
    @Test
    void verifyChangePasswordSuccess() {
        userService.changePassword(USERID_BRUCE_WAYNE, "wayne", "new_secret_1122");
        Optional<User> user = userService.findByIdentifier(USERID_BRUCE_WAYNE);
        assertThat(user).isPresent();
        assertThat(user.get().getPassword()).isEqualTo(passwordEncoder.encode("new_secret_1122"));
    }

    @DisplayName("2.1.6 Verify that password change requires the user's current and new password")
    @Test
    void verifyChangePasswordFailWrongOldPassword() {
        assertThatThrownBy(() -> userService.changePassword(
                USERID_BRUCE_WAYNE, "invalid", "new_secret_1122"
                )
        ).isInstanceOf(IllegalArgumentException.class);

        Optional<User> user = userService.findByIdentifier(USERID_BRUCE_WAYNE);
        assertThat(user).isPresent();
        assertThat(user.get().getPassword()).isEqualTo(passwordEncoder.encode("wayne"));
    }

    @AfterEach
    void cleanupTestData() {
        userEntityRepository.deleteAll();
    }
}
