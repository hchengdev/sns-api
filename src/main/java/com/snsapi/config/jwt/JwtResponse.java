package com.snsapi.config.jwt;

import lombok.Getter;

@Getter
public class JwtResponse {
    private final int id;
    private final String token;
    private final String email;

    public JwtResponse(int id, String token, String email) {
        this.id = id;
        this.token = token;
        this.email = email;
    }

}