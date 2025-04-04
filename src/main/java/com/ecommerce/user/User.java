package com.ecommerce.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.common.Audit;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String password;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    private String phone;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @Builder.Default
    @Column(name = "active")
    private boolean active = true;

    @Builder.Default
    @Column(name = "admin")
    private boolean admin = false;

    @Embedded
    private Audit audit;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return isAdmin() 
            ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            : Collections.emptyList();
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
        return isActive();
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

    public boolean isAdmin() {
        return Boolean.TRUE.equals(admin);
    }
}
