package com.example.bookmark.service;

import com.example.bookmark.data.CustomUserEntityRepository;
import com.example.bookmark.data.UserEntity;
import com.example.bookmark.data.UserEntityRepository;
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

    public UserService(UserEntityRepository userEntityRepository, CustomUserEntityRepository customUserEntityRepository) {
        this.userEntityRepository = userEntityRepository;
        this.customUserEntityRepository = customUserEntityRepository;
    }

    public Optional<User> findUserByEmail(String email) {
        return customUserEntityRepository.findUserByEmail(email).map(this::toUser);
    }

    public List<User> findAll() {
        return userEntityRepository.findAll().stream().map(this::toUser).collect(Collectors.toList());
    }

    private User toUser(UserEntity u) {
        return new User(
                u.getIdentifier(), u.getFirstName(), u.getLastName(),
                u.getPassword(), u.getEmail(), u.getRoles());
    }
}
