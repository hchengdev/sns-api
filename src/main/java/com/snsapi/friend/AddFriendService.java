package com.snsapi.friend;

import com.snsapi.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddFriendService  {
    private final IAddFriendRepository addFriendRepository;
    public void addFriend(Integer friendId, Integer user) {
        AddFriend addFriend = AddFriend.builder()
                .friend(User.builder().id(friendId).build())
                .user(User.builder().id(user).build())
                .status(Status.PENDING)
                .build();
        addFriendRepository.save(addFriend);
    }

    public void addFriendSucces(Integer friendId, Integer userId) {
        AddFriend addFriend = AddFriend.builder()
                .friend(User.builder().id(userId).build())
                .user(User.builder().id(friendId).build())
                .status(Status.ACCEPTED)
                .build();
        addFriendRepository.save(addFriend);
    }

    public void acceptFriend(Integer friendId, Integer userId) {
        AddFriend addFriend = addFriendRepository.findByUserIdAndFriendId(friendId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lời mời kết bạn"));
        if (addFriend.getStatus() == Status.PENDING) {
            addFriend.setStatus(Status.ACCEPTED);
            addFriendSucces(friendId, userId);
            addFriendRepository.save(addFriend);
        }
    }

    public void rejectFriend(Integer friendId, Integer userId) {
        AddFriend addFriend = addFriendRepository.findByUserIdAndFriendId(friendId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lời mời kết bạn"));
        addFriendRepository.delete(addFriend);
    }

    public List<Integer> mutualFriends(Integer user1, Integer user2) {
        return addFriendRepository.findMutualFriends(user1, user2);
    }
}
