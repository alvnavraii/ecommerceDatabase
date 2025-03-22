package com.ecommerce.municipality.dto;

import com.ecommerce.common.AuditResponse;
import com.ecommerce.municipality.Municipality;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MunicipalityResponse {
    private Long id;
    private String code;
    private String name;
    private String provinceName;
    private Boolean isActive;
    private AuditResponse audit;

    public static MunicipalityResponse fromEntity(Municipality entity) {
        return MunicipalityResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .provinceName(entity.getProvince().getName())
                .isActive(entity.getIsActive())
                .audit(AuditResponse.fromAudit(entity.getAudit()))
                .build();
    }
}
