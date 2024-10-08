package com.snsapi.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.snsapi.post.Post;
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
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email
    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @JsonProperty("password")
    @Column(nullable = false, length = 255)
    private String password;

    @JsonProperty("firstName")
    @Column(nullable = false, length = 255)
    private String firstName;

    @JsonProperty("lastName")
    @Column(nullable = false, length = 255)
    private String lastName;

    @JsonProperty("gender")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;  // Thay đổi ở đây

    @JsonProperty("profilePicture")
    private String profilePicture;

    @JsonProperty("coverPicture")
    @Column(nullable = false, length = 255)
    private String coverPicture;

    @JsonProperty("active")
    @Column(nullable = false)
    private boolean active;

    @JsonProperty("biography")
    @Column(nullable = false, length = 255)
    private String biography;

    @JsonProperty("birthday")
    @Column(nullable = false)
    private String birthday;

    @JsonProperty("address")
    @Column(nullable = false, length = 255)
    private String address;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    List<Post> posts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
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
}
