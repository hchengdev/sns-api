package com.snsapi.friend;

import com.snsapi.user.User;
import com.snsapi.user.UserDTO;
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

    @Query("SELECT CASE WHEN af.user.id = :userId THEN af.friend.id ELSE af.user.id END " +
            "FROM AddFriend af " +
            "WHERE (af.user.id = :userId OR af.friend.id = :userId) " +
            "AND af.status = 'ACCEPTED'")
    List<Integer> findAllFriendIds(@Param("userId") Integer userId);

    @Query("SELECT new com.snsapi.user.UserDTO(u.id, u.name, u.profilePicture) FROM User u WHERE u.id IN :friendIds")
    List<UserDTO> findFriendDetails(@Param("friendIds") List<Integer> friendIds);

    @Query("SELECT CASE WHEN af.user.id = :userId THEN af.friend.id ELSE af.user.id END " +
            "FROM AddFriend af " +
            "WHERE (af.user.id = :userId ) " +
            "AND af.status = 'PENDING'")
    List<Integer> findAllUserWaiting(@Param("userId") Integer userId);

    @Query("SELECT CASE WHEN af.user.id = :userId THEN af.friend.id ELSE af.user.id END " +
            "FROM AddFriend af " +
            "WHERE (af.friend.id = :userId ) " +
            "AND af.status = 'PENDING'")
    List<Integer> findAllFriendWaiting(@Param("userId") Integer userId);
}


