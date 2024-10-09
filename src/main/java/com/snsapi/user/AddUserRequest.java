package com.snsapi.user;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class AddUserRequest {
    private String email;
    private String password;
    private String name;
    private Integer phoneNumber;
    private Date birthday;
    private Boolean active;
    private Set<Role> roles;
}
