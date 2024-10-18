package com.snsapi.user;

import com.snsapi.exception.EmailAlreadyExistsException;
import com.snsapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${upload.image}")
    private String uploadDir;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findById(int id) throws UserNotFoundException {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UpdateUserRequest informationUser(User request) {
        return UpdateUserRequest.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .gender(request.getGender())
                .biography(request.getBiography())
                .birthday(request.getBirthday())
                .profilePicture(request.getProfilePicture()).build();
    }

    public void save(AddUserRequest request) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate1 = formatter.format(date);
        LocalDate sqlDate1 = java.sql.Date.valueOf(formattedDate1).toLocalDate();
        var user = User.builder()
                .email(request.getEmail())
                .password(encodePassword(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .birthday(request.getBirthday())
                .creationDate(sqlDate1)
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


    public void update(int id, FormUpdateRequest request) throws UserNotFoundException {
        User user = findById(id);

        user.setName(request.getName() != null ? request.getName() : user.getName());
        user.setGender(request.getGender() != null ? request.getGender() : user.getGender());
        user.setBiography(request.getBiography() != null ? request.getBiography() : user.getBiography());
        user.setPhone(request.getPhone() != null ? request.getPhone() : user.getPhone());
        user.setBirthday(request.getBirthday() != null ? request.getBirthday() : user.getBirthday());
        user.setAddress(request.getAddress() != null ? request.getAddress() : user.getAddress());

        MultipartFile profilePicture = request.getProfilePicture();
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String fileName = profilePicture.getOriginalFilename();
            File file = new File(uploadDir + fileName);
            try {
                profilePicture.transferTo(file);
                user.setProfilePicture(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving file");
            }
        }

        userRepository.save(user);
    }

    public void delete(int id) throws UserNotFoundException {
        findById(id);
        userRepository.deleteById(id);
    }

    public User updatePassword(int id, String newPassword, String oldPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(encodePassword(newPassword));
            return userRepository.save(user);
        }
        return null;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}

