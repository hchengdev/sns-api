package com.snsapi.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;



@Data
@Builder
public class UpdateUserRequest {
    private String name;
    private String phone;
    private Gender gender;
    private String email;
    private String biography;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String address;
    private String profilePicture;
}

