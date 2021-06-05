package com.example.bookmark.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findOneByIdentifier(UUID identifier);
    Optional<UserEntity> findUserByEmail(String email);
    void deleteByIdentifier(UUID identifier);
}
