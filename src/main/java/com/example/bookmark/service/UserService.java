package com.example.bookmark.service;

import com.example.bookmark.data.CustomUserEntityRepository;
import com.example.bookmark.data.UserEntity;
import com.example.bookmark.data.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final CustomUserEntityRepository customUserEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserEntityRepository userEntityRepository, CustomUserEntityRepository customUserEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.customUserEntityRepository = customUserEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findUserByEmail(String email) {
        return customUserEntityRepository.findUserByEmail(email).map(this::toUser);
    }

    public Optional<User> findByIdentifier(String userIdentifier) {
        return userEntityRepository.findOneByIdentifier(userIdentifier).map(
                this::toUser
        );
    }

    @Transactional
    public User createUser(User user) {
        return toUser(userEntityRepository.save(toUserEntity(user)));
    }

    @Transactional
    public void changePassword(String userIdentifier, String oldPassword, String newPassword) {
        userEntityRepository.findOneByIdentifier(userIdentifier).map(
                u -> {
                    u.setPassword(passwordEncoder.encode(newPassword));
                    userEntityRepository.save(u);
                    return u;
                }
        ).orElseThrow(() -> new IllegalArgumentException("No user found for identifier [" + userIdentifier + "]"));
    }

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

    @Transactional
    public void deleteUserByIdentifier(String userIdentifier) {
        userEntityRepository.deleteByIdentifier(userIdentifier);
    }
}
