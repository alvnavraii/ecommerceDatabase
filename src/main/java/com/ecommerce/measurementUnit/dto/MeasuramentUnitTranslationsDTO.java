package com.ecommerce.measurementUnit.dto;

import com.ecommerce.measurement_unit_translation.MeasurementUnitTranslation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeasuramentUnitTranslationsDTO {
    private Long id;
    private String name;

    public static MeasuramentUnitTranslationsDTO fromEntity(MeasurementUnitTranslation entity) {
        return MeasuramentUnitTranslationsDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
