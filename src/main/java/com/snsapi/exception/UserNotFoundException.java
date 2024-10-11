package com.snsapi.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {

        super("User not found with ID: " + id);
    }
}

