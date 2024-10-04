package com.snsapi.config.jwtservice;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtResponse {
    private final int id;
    private final String token;
    private final String type = "Bearer";
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtResponse(int id, String token, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.authorities = authorities;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
