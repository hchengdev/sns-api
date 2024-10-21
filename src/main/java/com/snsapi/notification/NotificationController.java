package com.snsapi.notification;

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
}
