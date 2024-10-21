package com.snsapi.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
public class FormUpdateRequest {
    private String name;
    private Gender gender;
    private String phone;
    private String biography;
    private LocalDate birthday;
    private String address;
    private Boolean active;
    private MultipartFile profilePicture;
}
