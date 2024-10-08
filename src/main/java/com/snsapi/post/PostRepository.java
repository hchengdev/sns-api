package com.snsapi.post;

import com.snsapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUser(User user);

    @Query("SELECT COUNT(u) FROM Post p JOIN p.likeUsers u WHERE p.id = :postId")
    long countLikesByPostId(Long postId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Post p JOIN p.likeUsers u " +
            "WHERE p.id = :postId AND u.id = :userId")
    boolean existsByPostIdAndUserId(Long postId, Long userId);


}
