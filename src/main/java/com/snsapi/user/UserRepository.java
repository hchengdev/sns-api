package com.snsapi.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(@Param("name") String name);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u JOIN ChatMessage cm ON (cm.sender.id = u.id OR cm.receiver.id = u.id) WHERE u.id <> ?1")
    List<User> findConversationsByUserId(Integer userId);
    @Query("SELECT u FROM User u JOIN u.sentMessages m WHERE m.receiver.id = :userId " +
            "OR m.sender.id = :userId")
    List<User> findFriendsByUserId(@Param("userId") Integer userId);
}

