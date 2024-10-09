package com.snsapi.user;

import com.snsapi.exception.EmailAlreadyExistsException;
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


    public User findById(int id) throws UserNotFoundException {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void save(AddUserRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(encodePassword(request.getPassword()))

                .name(request.getName())
                .phone(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .profilePicture("anh-ech-meme-hai-huoc_102044545.jpg")
                .active(request.getActive() != null ? request.getActive() : true)

                .roles(new HashSet<>(Set.of(Role.ROLE_USER)))
                .build();
        userRepository.save(user);
    }


    public void saveGG(String email) {
        var user = User.builder()
                .email(email)
                .password(encodePassword("123456789"))
                .name("clone username")
                .active(true)
                .roles(new HashSet<>(Set.of(Role.ROLE_USER)))

                .build();
        userRepository.save(user);
    }


    public void update(int id, UpdateUserRequest request) throws UserNotFoundException {
        User user = findById(id);

        Integer phoneNumber = request.getPhone();
        user.setName(request.getName() != null ? request.getName() : user.getName());
        user.setGender(request.getGender() != null ? request.getGender() : user.getGender());
        user.setBiography(request.getBiography() != null ? request.getBiography() : user.getBiography());
        user.setPhone(phoneNumber != null ? phoneNumber : user.getPhone());
        user.setBirthday(request.getBirthday() != null ? request.getBirthday() : user.getBirthday());
        user.setAddress(request.getAddress() != null ? request.getAddress() : user.getAddress());

        if (request.getProfilePicture() != null) {
            user.setProfilePicture(request.getProfilePicture());
        }

        userRepository.save(user);
    }


    public void delete(int id) throws UserNotFoundException {

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

