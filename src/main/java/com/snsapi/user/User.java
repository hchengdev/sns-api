package com.snsapi.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snsapi.role.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Email
    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "firstname", nullable = false, length = 255)
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 255)
    private String lastName;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "profile_picture", nullable = false, length = 255)
    private String profilePicture;

    @Column(name = "cover_picture", nullable = false, length = 255)
    private String coverPicture;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "biography", nullable = false, length = 255)
    private String biography;

    @Column(name = "birthday", nullable = false)
    private String birthday;

    @Column(name = "address", nullable = false, length = 255)
    private String address;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> roles = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<UserRole> roles = getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return getEmail();
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
        return true;
    }
}