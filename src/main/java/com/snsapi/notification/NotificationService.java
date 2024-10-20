package com.snsapi.notification;

import com.snsapi.comment.Comment;
import com.snsapi.post.Post;
import com.snsapi.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createLikePostNotification(User sender, User recipient, Post post) {
        if (!sender.equals(recipient)) {
            Notification notification = Notification.builder()
                    .sender(sender)
                    .recipient(recipient)
                    .message(sender.getUsername() + " đã thích bài đăng của bạn.")
                    .notificationType(Notification.NotificationType.LIKE_POST)
                    .post(post)
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
        }
    }

    public void createLikeCommentNotification(User sender, User recipient, Comment comment) {
        if (!sender.equals(recipient)) {
            Notification notification = Notification.builder()
                    .sender(sender)
                    .recipient(recipient)
                    .message(sender.getUsername() + " đã thích bình luận của bạn.")
                    .notificationType(Notification.NotificationType.LIKE_COMMENT)
                    .comment(comment)
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
        }
    }

    public void createCommentPostNotification(User sender, User recipient, Post post) {
        if (!sender.equals(recipient)) {
            Notification notification = Notification.builder()
                    .sender(sender)
                    .recipient(recipient)
                    .message(sender.getUsername() + " đã bình luận bài đăng của bạn.")
                    .notificationType(Notification.NotificationType.COMMENT_POST)
                    .post(post)
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
        }
    }

    public void createReplyCommentNotification(User sender, User recipient, Comment comment) {
        if (!sender.equals(recipient)) {
            Notification notification = Notification.builder()
                    .sender(sender)
                    .recipient(recipient)
                    .message(sender.getUsername() + " đã trả lời bình luận của bạn.")
                    .notificationType(Notification.NotificationType.REPLY_COMMENT)
                    .comment(comment)
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
        }
    }

    public List<Notification> getNotificationsForUser(User recipient) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(recipient);
    }

    public void markAllAsRead(User recipient) {
        List<Notification> notifications = notificationRepository.findByRecipientAndIsReadFalseOrderByCreatedAtDesc(recipient);
        notifications.forEach(Notification::markAsRead);
        notificationRepository.saveAll(notifications);
    }

    public Integer countUnreadNotificationsForUser(User recipient) {
        return notificationRepository.countByRecipientAndIsReadFalse(recipient);
    }
}
