package com.ecommerce.measurement_unit_translation.dto;

import com.ecommerce.language.Language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDto {
    private Long id;
    private String name;

    public static LanguageDto fromEntity(Language entity) {
        return LanguageDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
