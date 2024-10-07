package com.snsapi.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Email
    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @JsonProperty("password")
    @Column(nullable = false, length = 255)
    private String password;

    @JsonProperty("firstName")
    @Column(nullable = true, length = 255)
    private String firstName;

    @JsonProperty("lastName")
    @Column(nullable = true, length = 255)
    private String lastName;

    @JsonProperty("gender")
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Gender gender;

    @JsonProperty("profilePicture")
    private String profilePicture;

    @JsonProperty("coverPicture")
    @Column(nullable = true, length = 255)
    private String coverPicture;

    @JsonProperty("active")
    @Column(nullable = false)
    private boolean active;

    @JsonProperty("biography")
    @Column(nullable = true, length = 255)
    private String biography;

    @JsonProperty("birthday")
    @Column(nullable = true)
    private String birthday;

    @JsonProperty("address")
    @Column(nullable = true, length = 255)
    private String address;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public static User build(Optional<User> optionalUser) {
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .active(user.isEnabled())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .profilePicture(user.getProfilePicture())
                .coverPicture(user.getCoverPicture())
                .biography(user.getBiography())
                .birthday(user.getBirthday())
                .address(user.getAddress())
                .build();
    }

}
