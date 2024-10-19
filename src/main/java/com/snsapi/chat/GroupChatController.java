package com.snsapi.chat;


import com.snsapi.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/group")
public class GroupChatController {

    private final GroupChatService groupChatService;
    private final ChatService chatService;

    public GroupChatController(GroupChatService groupChatService, ChatService chatService) {
        this.groupChatService = groupChatService;
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public GroupChat createGroup(@RequestParam String name, @RequestParam Integer userId) {
        GroupChat groupChat = groupChatService.createGroup(name);
        groupChatService.addMember(groupChat.getId(), userId);
        return groupChat;
    }
    @PostMapping("/{groupId}/addMember")
    public GroupChat addMember(@PathVariable Integer groupId, @RequestBody Integer id) {
        return groupChatService.addMember(groupId, id);
    }

    @GetMapping("/{groupId}/messages")
    public List<GroupChatMessage> getMessages(@PathVariable Integer groupId) {
        return groupChatService.getMessages(groupId);
    }

    @GetMapping("/{groupId}/members")
    public Set<User> getMembers(@PathVariable Integer groupId) {
        return groupChatService.getMembers(groupId);
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return chatService.getAllUsers();
    }


    @GetMapping("/{userId}/groups")
    public ResponseEntity<List<GroupChat>> getUserGroups(@PathVariable Integer userId) {
        List<GroupChat> groups = groupChatService.getGroupsByUserId(userId);
        return ResponseEntity.ok(groups);
    }

}
