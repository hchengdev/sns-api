//package com.snsapi.config.jwtconfig;
//
//import com.snsapi.config.jwtservice.JwtService;
//import com.snsapi.token.TokenRepository;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
//    private final TokenRepository tokenRepository;
//
//    private static final String AUTH_HEADER = "Authorization";
//    private static final String BEARER_PREFIX = "Bearer ";
//
//    public JwtAuthenticationTokenFilter(JwtService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository) {
//        this.jwtService = jwtService;
//        this.userDetailsService = userDetailsService;
//        this.tokenRepository = tokenRepository;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if (request.getServletPath().contains("/api/v1/auth")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String authHeader = request.getHeader(AUTH_HEADER);
//        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String jwt = authHeader.substring(BEARER_PREFIX.length());
//        authenticateUser(jwt, request);
//
//        filterChain.doFilter(request, response);
//    }
//
//    private void authenticateUser(String jwt, HttpServletRequest request) {
//        String userEmail = jwtService.extractUsername(jwt);
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
//            boolean isTokenValid = tokenRepository.findByToken(jwt)
//                    .map(t -> !t.isExpired() && !t.isRevoked())
//                    .orElse(false);
//
//            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities()
//                );
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//    }
//}
