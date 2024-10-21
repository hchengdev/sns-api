package com.snsapi.notification;

import com.snsapi.comment.Comment;
import com.snsapi.comment.CommentService;
import com.snsapi.post.Post;
import com.snsapi.post.PostService;
import com.snsapi.user.User;
import com.snsapi.user.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final UserServices userServices;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam("userId") Integer userId) {
        User recipient = userServices.findById(userId);
        List<Notification> notifications = notificationService.getNotificationsForUser(recipient);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/mark-all-read")
    public ResponseEntity<Void> getMarkAllRead(@RequestParam("userId") Integer userId) {
        User recipient = userServices.findById(userId);
        notificationService.markAllAsRead(recipient);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like-post")
    public ResponseEntity<Void> likePost(@RequestParam("senderId") Integer senderId,
                                         @RequestParam("recipientId") Integer recipientId,
                                         @RequestParam("postId") Integer postId) {
        User sender = userServices.findById(senderId);
        User recipient = userServices.findById(recipientId);
        Post post = postService.findById(postId); // Giả sử bạn có PostService
        notificationService.createLikePostNotification(sender, recipient, post);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like-comment")
    public ResponseEntity<Void> likeComment(@RequestParam("senderId") Integer senderId,
                                            @RequestParam("recipientId") Integer recipientId,
                                            @RequestParam("commentId") Integer commentId) {
        User sender = userServices.findById(senderId);
        User recipient = userServices.findById(recipientId);
        Comment comment = commentService.findById(commentId); // Giả sử bạn có CommentService
        notificationService.createLikeCommentNotification(sender, recipient, comment);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comment-post")
    public ResponseEntity<Void> commentPost(@RequestParam("senderId") Integer senderId,
                                            @RequestParam("recipientId") Integer recipientId,
                                            @RequestParam("postId") Integer postId) {
        User sender = userServices.findById(senderId);
        User recipient = userServices.findById(recipientId);
        Post post = postService.findById(postId); // Giả sử bạn có PostService
        notificationService.createCommentPostNotification(sender, recipient, post);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reply-comment")
    public ResponseEntity<Void> replyComment(@RequestParam("senderId") Integer senderId,
                                             @RequestParam("recipientId") Integer recipientId,
                                             @RequestParam("commentId") Integer commentId) {
        User sender = userServices.findById(senderId);
        User recipient = userServices.findById(recipientId);
        Comment comment = commentService.findById(commentId); // Giả sử bạn có CommentService
        notificationService.createReplyCommentNotification(sender, recipient, comment);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countNotifications(@RequestParam("userId") Integer userId) {
        User recipient = userServices.findById(userId);
        Integer count = notificationService.countUnreadNotificationsForUser(recipient);
        return ResponseEntity.ok(count);
    }
}

