package com.example.bookmark.service;

import com.example.bookmark.data.UserEntity;
import com.example.bookmark.data.UserEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    Optional<User> findUserByEmail(String email) {
        return userEntityRepository.findUserByEmail(email).map(this::toUser);
    }

    @PreAuthorize("isAuthenticated()")
    public Optional<User> findByIdentifier(UUID userIdentifier) {
        return userEntityRepository.findOneByIdentifier(userIdentifier).map(
                this::toUser
        );
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public User createUser(User user) {
        if (user.getIdentifier() == null) {
            user.setIdentifier(UUID.randomUUID());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return toUser(userEntityRepository.save(toUserEntity(user)));
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void changePassword(UUID userIdentifier, String oldPassword, String newPassword) {
        UserEntity userEntity = userEntityRepository.findOneByIdentifier(userIdentifier).map(
                u -> {
                    if (!passwordEncoder.matches(oldPassword, u.getPassword())) {
                        throw new IllegalArgumentException("User and/or password is not valid");
                    }
                    u.setPassword(passwordEncoder.encode(newPassword));
                    userEntityRepository.save(u);
                    return u;
                }
        ).orElseThrow(() -> new IllegalArgumentException("User and/or password is not valid"));
        LOGGER.info("Successfully change password of user [{}] from [{}] to [{}]",
                userEntity.getEmail(), oldPassword, newPassword);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> findAll() {
        return userEntityRepository.findAll().stream().map(this::toUser).collect(Collectors.toList());
    }

    private User toUser(UserEntity u) {
        return new User(
                u.getIdentifier(), u.getFirstName(), u.getLastName(),
                u.getPassword(), u.getEmail(), u.getRoles());
    }

    private UserEntity toUserEntity(User u) {
        return new UserEntity(
                u.getIdentifier(), u.getFirstName(), u.getLastName(),
                u.getPassword(), u.getEmail(), u.getRoles());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUserByIdentifier(UUID userIdentifier) {
        userEntityRepository.deleteByIdentifier(userIdentifier);
    }
}
