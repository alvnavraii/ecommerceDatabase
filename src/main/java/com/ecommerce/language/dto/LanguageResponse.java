package com.ecommerce.language.dto;

import com.ecommerce.common.AuditResponse;
import com.ecommerce.language.Language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageResponse {
    private Long id;
    private String code;
    private String name;
    private Boolean isActive;
    private AuditResponse audit;

    public static LanguageResponse fromEntity(Language language) {
        return LanguageResponse.builder()
                .id(language.getId())
                .code(language.getCode())
                .name(language.getName())
                .isActive(language.getIsActive())
                .audit(AuditResponse.fromAudit(language.getAudit()))
                .build();
    }
}
