package com.ecommerce.autCom.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ecommerce.common.AuditResponse;
import com.ecommerce.autCom.AutCom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutComResponse {
    private Long id;
    private String code;
    private String alfaCode;
    private String name;
    private Boolean isActive;
    private List<ProvinceInfo> provinces;
    private AuditResponse audit;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvinceInfo {
        private Long id;
        private String code;
        private String alfaCode;
        private String name;
        private List<MunicipalityInfo> municipalities;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MunicipalityInfo {
        private Long id;
        private String code;
        private String name;
    }

    public static AutComResponse fromEntity(AutCom entity) {
        return AutComResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .alfaCode(entity.getAlfaCode())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .provinces(entity.getProvinces().stream()
                        .filter(province -> province.getIsActive())
                        .map(province -> ProvinceInfo.builder()
                                .id(province.getId())
                                .code(province.getCode())
                                .alfaCode(province.getAlfaCode())
                                .name(province.getName())
                                .municipalities(province.getMunicipalities().stream()
                                        .filter(municipality -> municipality.getIsActive())
                                        .map(municipality -> MunicipalityInfo.builder()
                                                .id(municipality.getId())
                                                .code(municipality.getCode())
                                                .name(municipality.getName())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .audit(AuditResponse.fromAudit(entity.getAudit()))
                .build();
    }
}