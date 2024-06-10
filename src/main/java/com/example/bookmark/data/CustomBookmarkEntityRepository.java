package com.example.bookmark.data;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomBookmarkEntityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<BookmarkEntity> findAllBookmarksByUser(String userIdentifier) {
        Query query = entityManager.createNativeQuery("select * from bookmark_entity b where b.user_identifier = '" + userIdentifier + "'", BookmarkEntity.class);
        return query.getResultList();
    }

    public Optional<BookmarkEntity> findOneBookmarkByIdentifier(String identifier) {
        try {
            Query query = entityManager.createNativeQuery("select * from bookmark_entity b where b.identifier = '" + identifier + "'", BookmarkEntity.class);
            return Optional.of((BookmarkEntity) query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public List<BookmarkEntity> findAllBookmarksByCategory(String category) {
        Query query = entityManager.createNativeQuery("select * from bookmark_entity b where b.category = '" + category + "'", BookmarkEntity.class);
        return query.getResultList();
    }

}
