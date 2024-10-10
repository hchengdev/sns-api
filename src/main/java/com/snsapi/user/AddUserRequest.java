package com.snsapi.user;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AddUserRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean active;
    private Set<Role> roles;
}

