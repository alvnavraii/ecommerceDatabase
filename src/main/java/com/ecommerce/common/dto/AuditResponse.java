package com.ecommerce.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.ecommerce.common.Audit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditResponse {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public static AuditResponse fromEntity(Audit audit) {
        if (audit == null) {
            return null;
        }

        return AuditResponse.builder()
                .createdAt(audit.getCreatedAt())
                .updatedAt(audit.getUpdatedAt())
                .createdBy(audit.getCreatedBy())
                .updatedBy(audit.getUpdatedBy())
                .build();
    }
}
