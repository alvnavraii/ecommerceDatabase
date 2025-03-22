package com.ecommerce.language.dto;

import com.ecommerce.language.Language;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageBasicResponse {
    private Long id;
    private String code;
    private Boolean isActive;

    public static LanguageBasicResponse fromEntity(Language entity) {
        if (entity == null) return null;
        return LanguageBasicResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .isActive(entity.getIsActive())
                .build();
    }
}
