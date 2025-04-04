package com.ecommerce.user;

import com.ecommerce.common.Audit;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.common.exception.OracleException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        return UserResponse.fromEntity(
                userRepository.findById(id)
                        .orElseThrow(() -> new OracleException("ORA-01403", "No se ha encontrado el usuario"))
        );
    }

    public UserResponse getUserByEmail(String email) {
        return UserResponse.fromEntity(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new OracleException("ORA-01403", "No se ha encontrado el usuario"))
        );
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        
        var audit = Audit.builder()
                .createdBy(currentUser)
                .createdAt(LocalDateTime.now())
                .updatedBy(currentUser)
                .updatedAt(LocalDateTime.now())
                .build();

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .avatarUrl(request.getAvatarUrl())
                .active(request.isActive())
                .admin(request.isAdmin())
                .audit(audit)
                .build();

        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se ha encontrado el usuario"));

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        updateUserFromRequest(user, request);

        user.getAudit().setUpdatedBy(currentUser);
        user.getAudit().setUpdatedAt(LocalDateTime.now());

        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se ha encontrado el usuario"));

        user.setActive(false);
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        user.getAudit().setUpdatedBy(currentUser);
        user.getAudit().setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    private void updateUserFromRequest(User user, UserRequest request) {
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        user.setActive(request.isActive());
        user.setAdmin(request.isAdmin());
    }
}
