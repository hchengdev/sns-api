package com.snsapi.friend;

import com.snsapi.config.jwt.JwtService;
import com.snsapi.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/me/friends")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AddFriendRestController {

    private final AddFriendService addFriendService;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserServices userServices;

    @PostMapping("/{id}")
    public ResponseEntity<?> addFriend(@PathVariable("id") Integer userId, @RequestHeader("Authorization") String token) {
        try {
            token = token.startsWith("Bearer") ? token.substring(7) : token;
            Integer id = jwtService.getUserIdFromToken(token);
            addFriendService.pendingFriend(userId, id);
            return ResponseEntity.ok("Gửi yêu cầu kết bạn thành công.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Thêm bạn bè thất bại.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> acceptFriend(@PathVariable("id") Integer userId, @RequestHeader("Authorization") String token) {
        try {
            token = token.startsWith("Bearer") ? token.substring(7) : token;
            int id = jwtService.getUserIdFromToken(token);
            addFriendService.acceptFriend( id, userId);
            return ResponseEntity.ok("Chấp nhận kết bạn thành công.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Chấp nhận kết bạn thất bại.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> rejectFriend(@PathVariable("id") Integer userId, @RequestHeader("Authorization") String token) {
        try {
            token = token.startsWith("Bearer") ? token.substring(7) : token;
            int id = jwtService.getUserIdFromToken(token);
            addFriendService.rejectFriend(userId, id);
            return ResponseEntity.ok("Từ chối kết bạn thành công.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Từ chối kết bạn thất bại.");
        }
    }

    @GetMapping("/mutual/{id}")
    public ResponseEntity<?> mutualFriends(@PathVariable("id") Integer friendId, Principal principal) {
        try {
            String user = principal.getName();
            Optional<User> user1 = userService.findByUserEmail(user);
            if (user1.isPresent()) {
                Integer id = user1.get().getId();
                List<Integer> mutualFriends = addFriendService.mutualFriends(friendId, id);

                if (mutualFriends.isEmpty()) {
                    return ResponseEntity.ok("Không có bạn chung.");
                }
                return ResponseEntity.ok(mutualFriends);

            } else {
                return ResponseEntity.ok("Không tìm thấy tài khoản.");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lấy danh sách bạn chung thất bại.");
        }
    }



}
