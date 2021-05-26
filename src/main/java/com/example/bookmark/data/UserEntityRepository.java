package com.example.bookmark.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneByIdentifier(String identifier);

    void deleteByIdentifier(String identifier);

}
