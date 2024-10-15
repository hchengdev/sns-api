package com.snsapi.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindUserResponse {
    private Integer id;
    private String name;
    private String email;
    private Boolean active;
    private String profilePicture;
    private String biography;
    private String address;
}
