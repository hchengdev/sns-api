package com.snsapi.friend;

import com.snsapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAddFriendRepository extends JpaRepository<AddFriend, Integer> {
    Optional<AddFriend> findByUserIdAndFriendId(Integer userId, Integer friendId);

    @Query("SELECT uf1.friend.id AS mutualFriendId " +
            "FROM AddFriend uf1 " +
            "INNER JOIN AddFriend uf2 ON uf1.friend.id = uf2.friend.id " +
            "WHERE uf1.user.id = :userId1 " +
            "AND uf2.user.id = :userId2 " +
            "AND uf1.status = 'ACCEPTED' " +
            "AND uf2.status = 'ACCEPTED'")
    List<Integer> findMutualFriends(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);

    @Query("SELECT u FROM User u join AddFriend af on u.id = af.friend.id WHERE (af.user.id = :user OR af.friend.id = :user) AND af.status = 'ACCEPTED'")
    List<User> findAllAcceptedFriends(@Param("user") Integer userId);
}
