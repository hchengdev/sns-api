package com.snsapi.user;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    List<User> findAll();

    User findById(int id);


    User save(User user);

    void delete(User user);

    UserDetails loadUserByUsername(String username);

    Optional<User> findByUserEmail(String username);
}

