package com.snsapi.user;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

import java.util.Set;

@Data
@Builder
public class UpdateUserRequest {

    private String name;
    private Integer phone;
    private Gender gender;
    private String biography;
    private Date birthday;
    private String address;
    private String profilePicture;
}

