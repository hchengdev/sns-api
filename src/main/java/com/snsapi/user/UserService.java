//package com.snsapi.user;
//
//import com.snsapi.exception.UserNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//    private final UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public Page<User> findAll(Pageable pageable) {
//        return userRepository.findAll(pageable);
//    }
//
//    public User findById(Long id) throws UserNotFoundException {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException(id));
//    }
//
//    public void save(UserRequest request) {
//        var user = User.builder()
//                .id(request.getId())
//                .email(request.getEmail())
//                .password(encodePassword(request.getPassword()))
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .gender(request.getGender())
//                .profilePicture(request.getProfilePicture())
//                .coverPicture(request.getCoverPicture())
//                .active(request.getActive() != null ? request.getActive() : true)
//                .biography(request.getBiography())
//                .birthday(request.getBirthday())
//                .address(request.getAddress())
//                .roles(new HashSet<>(Set.of(Role.ROLE_USER)))
//                .build();
//        userRepository.save(user);
//    }
//
//    public void save(Long id, UserRequest request) throws UserNotFoundException {
//        User user = findById(id);
//
//        var updatedUser = User.builder()
//                .id(user.getId())
//                .email(request.getEmail() != null ? request.getEmail() : user.getEmail())
//                .password(request.getPassword() != null ? encodePassword(request.getPassword()) : user.getPassword())
//                .firstName(request.getFirstName() != null ? request.getFirstName() : user.getFirstName())
//                .lastName(request.getLastName() != null ? request.getLastName() : user.getLastName())
//                .gender(request.getGender() != null ? request.getGender() : user.getGender())
//                .profilePicture(request.getProfilePicture() != null ? request.getProfilePicture() : user.getProfilePicture())
//                .coverPicture(request.getCoverPicture() != null ? request.getCoverPicture() : user.getCoverPicture())
//                .active(request.getActive() != null ? request.getActive() : user.isActive())
//                .biography(request.getBiography() != null ? request.getBiography() : user.getBiography())
//                .birthday(request.getBirthday() != null ? request.getBirthday() : user.getBirthday())
//                .address(request.getAddress() != null ? request.getAddress() : user.getAddress())
//                .roles(request.getRoles() != null && !request.getRoles().isEmpty() ? new HashSet<>(request.getRoles()) : user.getRoles())
//                .build();
//
//        userRepository.save(updatedUser);
//    }
//
//    public void delete(Long id) throws UserNotFoundException {
//        findById(id);
//        userRepository.deleteById(id);
//    }
//
//    public Optional<User> findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//    public void register(User user) {
//        user.setPassword(encodePassword(user.getPassword()));
//        user.addRole(Role.ROLE_USER);
//        user.setActive(true);
//        userRepository.save(user);
//    }
//
//    private String encodePassword(String password) {
//        return passwordEncoder.encode(password);
//    }
//}
