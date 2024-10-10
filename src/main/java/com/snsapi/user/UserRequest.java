package com.snsapi.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserRequest {
    private Integer id;
    private String email;
    private String password;
   private String name;
    private String phone;
    private Gender gender;
    private String profilePicture;
    private String coverPicture;
    private Boolean active;
    private String biography;
    private LocalDate birthday;
    private String address;
    private Set<Role> roles;
}