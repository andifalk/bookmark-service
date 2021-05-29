package com.example.bookmark.security.unit.inputvalidation;

import com.example.bookmark.security.annotation.UnitTest;
import com.example.bookmark.service.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
@DisplayName("V5.1 Input Validation Requirements")
class UserInputValidationTest {

    static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("5.1.4 Verify that structured data is strongly typed and validated.")
    @Nested
    class InputValidationTests {

        @DisplayName("Verify that valid user names are accepted as expected.")
        @ValueSource(strings = {"abc@example.com", "123@example.com",
                "123456789012345678901234567890@example.de", "ab23cd_56jh78@example.com"})
        @ParameterizedTest
        void verifyValidUsernames(String username) {

            User user = createUser(username);

            Set<ConstraintViolation<User>> violations = validator.validate(user);
            assertThat(violations).isEmpty();
        }

        @DisplayName("Verify that invalid user names are denied.")
        @ValueSource(strings = {"12", "aa@", "1 2", "test@abc.", "test@abc", "1234567890123456789012345678901",
                "<javascript>alert('xss')</javascript>", "or 1=1 --", "$%/_12345", "@"})
        @NullAndEmptySource
        @ParameterizedTest
        void verifyInvalidUsernames(String username) {

            User user = createUser(username);

            Set<ConstraintViolation<User>> violations = validator.validate(user);
            assertThat(violations).isNotEmpty();
        }

        private User createUser(String username) {
            return new User(
                    UUID.randomUUID().toString(),
                    "firstname",
                    "lastname",
                    "My_VerySecurePassw0rd",
                    username,
                    List.of("USER"));
        }
    }
}
