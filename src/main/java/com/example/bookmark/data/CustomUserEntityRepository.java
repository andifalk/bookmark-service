package com.example.bookmark.data;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomUserEntityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public Optional<UserEntity> findUserByEmail(String email) {
        String sql = "select * from user_entity u where u.email = '" + email + "'";
        Query query = entityManager.createNativeQuery(sql, UserEntity.class);
        List<UserEntity> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(resultList.get(0));
        }
    }

}
