package com.snsapi.user;

public class UserConverter {

    public static UpdateUserRequest toUpdateUserRequest(UserForm userForm) {
        return UpdateUserRequest.builder()
                .name(userForm.getName())
                .phone(userForm.getPhone())
                .gender(userForm.getGender())
                .biography(userForm.getBiography())
                .birthday(userForm.getBirthday())
                .address(userForm.getAddress())
                .profilePicture(userForm.getProfilePicture() != null ? userForm.getProfilePicture().getOriginalFilename() : null)
                .build();
    }

    public static UserForm toUserForm(UpdateUserRequest updateUserRequest) {
        return UserForm.builder()
                .name(updateUserRequest.getName())
                .phone(updateUserRequest.getPhone())
                .gender(updateUserRequest.getGender())
                .biography(updateUserRequest.getBiography())
                .birthday(updateUserRequest.getBirthday())
                .active(true)
                .build();
    }
}
