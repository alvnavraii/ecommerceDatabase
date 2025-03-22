package com.ecommerce.measurement_unit_translation.dto;

import com.ecommerce.measurementUnit.MeasurementUnit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMeasurementUnitResponse {
    private Long id;
    private String name;
    private String symbol;

    public static SimpleMeasurementUnitResponse fromEntity(MeasurementUnit entity) {
        return SimpleMeasurementUnitResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .symbol(entity.getSymbol())
                .build();
    }
}
