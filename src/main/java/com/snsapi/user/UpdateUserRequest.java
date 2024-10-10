package com.snsapi.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@Builder
public class UpdateUserRequest {
    private String name;
    private String phone;
    private Gender gender;
    private String biography;
    private LocalDate birthday;
    private String address;
    private String profilePicture;
}
