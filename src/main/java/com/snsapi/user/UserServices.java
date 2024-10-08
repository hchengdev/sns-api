package com.snsapi.user;

import com.snsapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findById(Integer id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void save(AddUserRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(encodePassword(request.getPassword()))
                .firstName("clone")
                .lastName("user")
                .active(request.getActive() != null ? request.getActive() : true)
                .roles(new HashSet<>(Set.of(Role.ROLE_USER)))
                .build();
        userRepository.save(user);
    }

    public void update(Integer id, UpdateUserRequest request) throws UserNotFoundException {
        User user = findById(id);
        var updatedUser = User.builder()
                .email(request.getEmail() != null ? request.getEmail() : user.getEmail())
                .password(request.getPassword() != null ? encodePassword(request.getPassword()) : user.getPassword())
                .firstName(request.getFirstName() != null ? request.getFirstName() : user.getFirstName())
                .lastName(request.getLastName() != null ? request.getLastName() : user.getLastName())
                .gender(request.getGender() != null ? request.getGender() : user.getGender())
                .profilePicture(request.getProfilePicture() != null ? request.getProfilePicture() : user.getProfilePicture())
                .coverPicture(request.getCoverPicture() != null ? request.getCoverPicture() : user.getCoverPicture())
                .active(request.getActive() != null ? request.getActive() : user.isActive())
                .biography(request.getBiography() != null ? request.getBiography() : user.getBiography())
                .birthday(request.getBirthday() != null ? request.getBirthday() : user.getBirthday())
                .address(request.getAddress() != null ? request.getAddress() : user.getAddress())
                .roles(request.getRoles() != null && !request.getRoles().isEmpty() ? new HashSet<>(request.getRoles()) : user.getRoles())
                .build();

        userRepository.save(updatedUser);
    }

    public void delete(Integer id) throws UserNotFoundException {
        findById(id);
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}