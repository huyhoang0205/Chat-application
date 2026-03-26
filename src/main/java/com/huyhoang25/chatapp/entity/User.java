package com.huyhoang25.chatapp.entity;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default // set default value for field
    private List<UserHasRole> userHasRoles = new ArrayList<>();

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = this.userHasRoles.stream()
                    .map(UserHasRole::getRole).toList();
        
        return roles.stream()
            .map(r -> new SimpleGrantedAuthority(r.getName()))
            .toList();
    }

    public void addRole(Role role) {
        this.userHasRoles.add(UserHasRole.builder()
                .user(this)
                .role(role)
                .build());
    }
    
    @Override
    public String getUsername() {
        return this.username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
    
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
    
}
