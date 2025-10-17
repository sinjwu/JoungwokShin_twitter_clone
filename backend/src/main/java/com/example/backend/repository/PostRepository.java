package com.example.backend.repository;

import com.example.backend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.author ORDER BY p.createdAt DESC")
    Page<Post> findAllWithAuthor(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.author " +
            "LEFT JOIN FETCH p.likes " +
            "WHERE p.id = :postId")
    Optional<Post> findByIdWithAuthorAndLikes(@Param("postId") Long postId);

    @Query("SELECT p FROM Post p WHERE p.author.id = :userId ORDER BY p.createdAt DESC")
    Page<Post> findByAuthorId(@Param("userId") Long userId, Pageable pageable);
}
