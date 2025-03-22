package com.ecommerce.measurementUnit.dto;

import com.ecommerce.common.AuditResponse;
import com.ecommerce.measurementUnit.MeasurementUnit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementUnitResponse {
    private Long id;
    private String code;
    private String name;
    private String symbol;
    private Boolean isActive;
    private AuditResponse audit;
    private List<MeasuramentUnitTranslationsDTO> translations;
    public static MeasurementUnitResponse fromEntity(MeasurementUnit entity) {
        return MeasurementUnitResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .symbol(entity.getSymbol())
                .isActive(entity.getIsActive())
                .audit(AuditResponse.fromAudit(entity.getAudit()))
                .translations(entity.getTranslations().stream().map(MeasuramentUnitTranslationsDTO::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
