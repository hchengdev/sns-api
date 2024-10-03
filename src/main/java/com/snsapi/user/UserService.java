package com.snsapi.user;

import com.snsapi.role.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findById(Long id) throws ChangeSetPersister.NotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    public void save(UserRequest request) {
        var user = User.builder()
                .id(request.getId())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .build();
        userRepository.save(user);
    }

    public void save(Long id, UserRequest request) throws ChangeSetPersister.NotFoundException {
        Optional<User> userOptional = findById(id);
        if (userOptional.isEmpty()) {
            throw new ChangeSetPersister.NotFoundException();
        }

        User user = userOptional.get();
        var updatedUser = User.builder()
                .id(user.getId())
                .email(request.getEmail() == null ? user.getEmail() : request.getEmail())
                .password(request.getPassword() == null ? user.getPassword() : request.getPassword())
                .firstName(request.getFirstName() == null ? user.getFirstName() : request.getFirstName())
                .lastName(request.getLastName() == null ? user.getLastName() : request.getLastName())
                .gender(request.getGender() == null ? user.getGender() : request.getGender())
                .profilePicture(request.getProfilePicture() == null ? user.getProfilePicture() : request.getProfilePicture())
                .coverPicture(request.getCoverPicture() == null ? user.getCoverPicture() : request.getCoverPicture())
                .active(request.getActive() == null ? user.isActive() : request.getActive())
                .biography(request.getBiography()== null ? user.getBiography() : request.getBiography())
                .birthday(request.getBirthday() == null ? user.getBirthday() : request.getBirthday())
                .address(request.getAddress() == null ? user.getAddress() : request.getAddress())
                .roles(request.getRoles() == null || request.getRoles().isEmpty() ? user.getRoles() : request.getRoles())
                .build();

        userRepository.save(updatedUser);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
