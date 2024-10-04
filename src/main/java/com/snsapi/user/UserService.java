package com.snsapi.user;

import com.snsapi.exception.UserAlreadyExistsException;
import com.snsapi.exception.UserNotFoundException;
import com.snsapi.role.UserRole;
import com.snsapi.role.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void save(UserRequest request) {
        var user = User.builder()
                .id(request.getId())
                .email(request.getEmail())
                .password(encodePassword(request.getPassword()))
                .roles(request.getRoles())
                .build();
        userRepository.save(user);
    }

    public void save(Long id, UserRequest request) throws UserNotFoundException {
        User user = findById(id);

        var updatedUser = User.builder()
                .id(user.getId())
                .email(request.getEmail() == null ? user.getEmail() : request.getEmail())
                .password(request.getPassword() == null ? user.getPassword() : encodePassword(request.getPassword()))
                .firstName(request.getFirstName() == null ? user.getFirstName() : request.getFirstName())
                .lastName(request.getLastName() == null ? user.getLastName() : request.getLastName())
                .gender(request.getGender() == null ? user.getGender() : request.getGender())
                .profilePicture(request.getProfilePicture() == null ? user.getProfilePicture() : request.getProfilePicture())
                .coverPicture(request.getCoverPicture() == null ? user.getCoverPicture() : request.getCoverPicture())
                .active(request.getActive() == null ? user.isActive() : request.getActive())
                .biography(request.getBiography() == null ? user.getBiography() : request.getBiography())
                .birthday(request.getBirthday() == null ? user.getBirthday() : request.getBirthday())
                .address(request.getAddress() == null ? user.getAddress() : request.getAddress())
                .roles(request.getRoles() == null || request.getRoles().isEmpty() ? user.getRoles() : request.getRoles())
                .build();

        userRepository.save(updatedUser);
    }

    public void delete(Long id) throws UserNotFoundException {
        findById(id);
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Người dùng đã tồn tại!");
        }
        user.setPassword(encodePassword(user.getPassword()));
        UserRole defaultRole = userRoleRepository.findByName("ROLE_USER");
        user.addRole(defaultRole);
        user.setActive(true);
        userRepository.save(user);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
