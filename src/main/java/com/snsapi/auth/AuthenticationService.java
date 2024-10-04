//package com.snsapi.auth;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.snsapi.config.jwtservice.JwtServices;
//import com.snsapi.token.Token;
//import com.snsapi.token.TokenRepository;
//import com.snsapi.token.TokenType;
//import com.snsapi.user.User;
//import com.snsapi.user.UserRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//    private final UserRepository repository;
//    private final TokenRepository tokenRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtServices jwtService;
//    private final AuthenticationManager authenticationManager;
//    private final ObjectMapper objectMapper = new ObjectMapper(); // Tạo một ObjectMapper duy nhất
//
//    public AuthenticationResponse register(RegisterRequest request) {
//        User user = buildUser(request);
//        User savedUser = repository.save(user);
//        return createAuthResponse(savedUser);
//    }
//
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );
//        User user = repository.findByEmail(request.getEmail())
//                .orElseThrow();
//        return createAuthResponse(user);
//    }
//
//    private AuthenticationResponse createAuthResponse(User user) {
//        String jwtToken = jwtService.generateToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//    private User buildUser(RegisterRequest request) {
//        return User.builder()
//                .firstName(request.getFirstname())
//                .lastName(request.getLastname())
//                .gender(request.getGender())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .build();
//    }
//
//    private void saveUserToken(User user, String jwtToken) {
//        Token token = Token.builder()
//                .user(user)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepository.save(token);
//    }
//
//    private void revokeAllUserTokens(User user) {
//        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//        if (!validUserTokens.isEmpty()) {
//            validUserTokens.forEach(token -> {
//                token.setExpired(true);
//                token.setRevoked(true);
//            });
//            tokenRepository.saveAll(validUserTokens);
//        }
//    }
//
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        String refreshToken = authHeader.substring(7);
//        String userEmail = jwtService.extractUsername(refreshToken);
//        if (userEmail != null) {
//            User user = repository.findByEmail(userEmail).orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                String accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                writeAuthResponse(response, accessToken, refreshToken);
//            }
//        }
//    }
//
//    private void writeAuthResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
//        AuthenticationResponse authResponse = AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//        objectMapper.writeValue(response.getOutputStream(), authResponse);
//    }
//}
