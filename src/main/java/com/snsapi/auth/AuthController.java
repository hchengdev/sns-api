package com.snsapi.auth;

import com.snsapi.config.jwtservice.JwtResponse;
import com.snsapi.config.jwtservice.JwtService;
import com.snsapi.user.User;
import com.snsapi.user.UserDetailsServiceImpl;
import com.snsapi.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
                          UserDetailsServiceImpl userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> currentUserOptional = userService.findByEmail(user.getEmail());

            if (!currentUserOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Người dùng không tìm thấy");
            }

            User currentUser = currentUserOptional.get();

            if (!currentUser.isEnabled()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Người dùng bị vô hiệu hóa");
            }

            return ResponseEntity.ok(new JwtResponse(currentUser.getId(), jwt, currentUser.getEmail(), userDetails.getAuthorities()));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Người dùng bị vô hiệu hóa");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
