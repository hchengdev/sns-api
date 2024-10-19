package com.snsapi.chat;

import com.snsapi.user.User;
import com.snsapi.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final GroupChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public GroupChatService(GroupChatRepository groupChatRepository, GroupChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.groupChatRepository = groupChatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    public GroupChat createGroup(String name) {
        Optional<GroupChat> existingGroup = groupChatRepository.findByName(name);
        if (existingGroup.isPresent()) {
            return existingGroup.get();
        }
        GroupChat groupChat = new GroupChat();
        groupChat.setName(name);
        groupChat.setMembers(new HashSet<>());
        return groupChatRepository.save(groupChat);
    }

    public GroupChat addMember(Integer groupId, Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        GroupChat groupChat = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("GroupChat not found"));

        groupChat.getMembers().add(user);
        return groupChatRepository.save(groupChat);
    }


    public GroupChatMessage saveMessage(GroupChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<GroupChatMessage> getMessages(Integer groupId) {
        return chatMessageRepository.findByGroupChatId(groupId);
    }

    public Set<User> getMembers(Integer groupId) {
        return groupChatRepository.findById(groupId).orElseThrow().getMembers();
    }
    public List<GroupChat> getGroupsByUserId(Integer userId) {
        return groupChatRepository.findGroupsByUserId(userId);
    }
    public boolean isUserInGroup(Integer groupId, Integer userId) {
        Set<User> members = getMembers(groupId);
        return members.stream().anyMatch(member -> member.getId().equals(userId));
    }

}