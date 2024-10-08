package com.snsapi.user;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UpdateUserRequest {
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String profilePicture;
    private String coverPicture;
    private Boolean active;
    private String biography;
    private String birthday;
    private String address;
    private Set<Role> roles;
}
