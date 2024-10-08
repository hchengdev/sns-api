package com.snsapi.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    Optional<User> findById(Integer id);

    User save(User user);

    void deleteById(Integer id);

    Optional<User> findByEmail(String email);

}