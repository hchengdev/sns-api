package com.snsapi.user;

import com.snsapi.role.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserRequest {
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private String profilePicture;
    private String coverPicture;
    private Boolean active;
    private String biography;
    private String birthday;
    private String address;
    private Set<UserRole> roles;
}
