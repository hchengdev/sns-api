package com.snsapi.config.jwt;

import lombok.Getter;

@Getter
public class JwtResponse {
    private final int id;
    private final String token;
    private final String name;
    private final String profilePicture;
    private final String role;

    public JwtResponse(int id, String token, String name, String profilePicture, String role) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.profilePicture = profilePicture;
        this.role = role;
    }


}

