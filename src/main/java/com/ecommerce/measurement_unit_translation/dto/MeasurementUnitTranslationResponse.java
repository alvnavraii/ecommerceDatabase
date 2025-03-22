package com.ecommerce.measurement_unit_translation.dto;

import com.ecommerce.common.AuditResponse;
import com.ecommerce.measurement_unit_translation.MeasurementUnitTranslation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementUnitTranslationResponse {
    private Long id;
    private String name;
    private SimpleMeasurementUnitResponse measurementUnit;
    private LanguageDto language;
    private Boolean isActive;
    private AuditResponse audit;

    public static MeasurementUnitTranslationResponse fromEntity(MeasurementUnitTranslation entity) {
        return MeasurementUnitTranslationResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .measurementUnit(SimpleMeasurementUnitResponse.fromEntity(entity.getMeasurementUnit()))
                .language(LanguageDto.fromEntity(entity.getLanguage()))
                .audit(AuditResponse.fromAudit(entity.getAudit()))
                .build();
    }
}
