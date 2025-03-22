package com.ecommerce.user.dto;

import com.ecommerce.common.AuditResponse;
import com.ecommerce.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String avatarUrl;
    private boolean isActive;
    private boolean isAdmin;
    private AuditResponse audit;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .isActive(user.getIsActive() == 1)
                .isAdmin(user.getIsAdmin() == 1)
                .audit(AuditResponse.fromAudit(user.getAudit()))
                .build();
    }
}
