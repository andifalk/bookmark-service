package com.example.bookmark.security.integration.brokenauthentication;

import com.example.bookmark.data.BookmarkEntityRepository;
import com.example.bookmark.data.UserEntity;
import com.example.bookmark.data.UserEntityRepository;
import com.example.bookmark.security.annotation.IntegrationTest;
import com.example.bookmark.security.util.TestDataUtil;
import com.example.bookmark.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.servlet.Filter;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@IntegrationTest
@DisplayName("V2.1 Password Security Requirements")
@SpringBootTest(webEnvironment = NONE)
public class ChangePasswordVerificationTests {

    @MockBean(name = "springSecurityFilterChain")
    Filter springSecurityFilterChain;

    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @BeforeEach
    void initTestData() {
        TestDataUtil.createUsers().forEach(u -> userEntityRepository.save(u));
    }

    @AfterEach
    void cleanupTestData() {
        userEntityRepository.deleteAll();
    }


}
