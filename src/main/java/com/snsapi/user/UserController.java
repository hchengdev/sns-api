package com.snsapi.user;

import com.snsapi.config.jwt.JwtService;
import com.snsapi.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserServices userService;
    private final UserServiceInterface userDetailsService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtService jwtService, UserServiceInterface userDetailsService, UserServices userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping("/api/v1/register")
    public ResponseEntity<?> register(@RequestBody AddUserRequest request) {
        try {
            userService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Đăng ký thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @GetMapping("api/v1/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        userService.findById(id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/api/v1/usersUpdate/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody UpdateUserRequest updateRequest) {
        try {
            userService.update(id, updateRequest);
            return ResponseEntity.ok("User updated successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
