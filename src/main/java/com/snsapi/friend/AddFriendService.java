package com.snsapi.friend;

import com.snsapi.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddFriendService  {
    private final IAddFriendRepository addFriendRepository;
    private final UserRepository userRepository;

    public AddFriend addPending(Integer friendId, Integer user) {
        AddFriend addFriend = AddFriend.builder()
                .friend(User.builder().id(friendId).build())
                .user(User.builder().id(user).build())
                .status(Status.PENDING)
                .build();
        return addFriend;
    }

    public void pendingFriend(Integer friendId, Integer userId) {
        AddFriend addFriend2 = addPending(friendId, userId);
        addFriendRepository.save(addFriend2);
    }

    public void addFriendSuccess(Integer friendId, Integer userId) {
        AddFriend addFriend = AddFriend.builder()
                .friend(User.builder().id(userId).build())
                .user(User.builder().id(friendId).build())
                .status(Status.ACCEPTED)
                .build();
        addFriendRepository.save(addFriend);
    }

    public void acceptFriend(Integer friendId, Integer userId) {

        AddFriend addFriend = addFriendRepository.findByUserIdAndFriendId(userId, friendId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lời mời kết bạn"));
        if (addFriend.getStatus() == Status.PENDING) {
            addFriend.setStatus(Status.ACCEPTED);
            addFriendRepository.save(addFriend);
            addFriendSuccess(friendId, userId);
        }
    }

    public void rejectFriend(Integer friendId, Integer userId) {
        addFriendRepository.findByUserIdAndFriendId(friendId, userId)
                .ifPresent(addFriendRepository::delete);

        addFriendRepository.findByUserIdAndFriendId(userId, friendId)
                .ifPresent(addFriendRepository::delete);
    }

    public List<Integer> mutualFriends(Integer user1, Integer user2) {
        return addFriendRepository.findMutualFriends(user1, user2);
    }

    public List<UserDTO> findAllFriends(Integer userId) {
        List<Integer> friendIds = addFriendRepository.findAllFriendIds(userId);
        if (!friendIds.isEmpty()) {
            List<UserDTO> friends = addFriendRepository.findFriendDetails(friendIds);
            return friends;
        }
        return Collections.emptyList();    }

    public List<UserDTO> findAllFriendsByStatus(Integer userId) throws AccessDeniedException {
        User user = userRepository.findById(userId).orElseThrow();

        if (user.getStatusFriend() == StatusFriend.PUBLIC) {
            return findAllFriends(userId);
        }
        if (user.getStatusFriend() == StatusFriend.PRIVATE) {
            throw new AccessDeniedException("Không có quyền truy cập để xem bạn bè.");
        }
        if (user.getStatusFriend() == StatusFriend.FRIEND) {
            if (user.isFriend(user)) {
                return findAllFriends(userId);
            } else {
                return Collections.emptyList();
            }
        }

        return Collections.emptyList();
    }

    public List<UserDTO> findAllWaitingUser(Integer userId) {
        List<Integer> waitingUserIds = addFriendRepository.findAllUserWaiting(userId);
        if (!waitingUserIds.isEmpty()) {
            List<UserDTO> findAllWaitingUser = addFriendRepository.findFriendDetails(waitingUserIds);
            return findAllWaitingUser;
        }
        return Collections.emptyList();
    }



    public List<UserDTO> findAllFriendWaiting(Integer userId) {
        List<Integer> waitingUserIds = addFriendRepository.findAllFriendWaiting(userId);
        if (!waitingUserIds.isEmpty()) {
            List<UserDTO> findAllWaitingFriend = addFriendRepository.findFriendDetails(waitingUserIds);
            return findAllWaitingFriend;
        }
        return Collections.emptyList();
    }

}
