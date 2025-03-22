package com.ecommerce.province.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ecommerce.common.AuditResponse;
import com.ecommerce.municipality.Municipality;
import com.ecommerce.province.Province;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceResponse {
    private Long id;
    private String code;
    private String alfaCode;
    private String name;
    private List<MunicipalityInfo> municipalities;
    private Boolean isActive;
    private AuditResponse audit;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MunicipalityInfo {
        private Long id;
        private String code;
        private String name;
    }

    public static ProvinceResponse fromEntity(Province entity) {
        return ProvinceResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .alfaCode(entity.getAlfaCode())
                .name(entity.getName())
                .municipalities(entity.getMunicipalities().stream()
                        .filter(Municipality::getIsActive)
                        .map(m -> MunicipalityInfo.builder()
                                .id(m.getId())
                                .code(m.getCode())
                                .name(m.getName())
                                .build())
                        .collect(Collectors.toList()))
                .isActive(entity.getIsActive())
                .audit(AuditResponse.fromAudit(entity.getAudit()))
                .build();
    }
}
