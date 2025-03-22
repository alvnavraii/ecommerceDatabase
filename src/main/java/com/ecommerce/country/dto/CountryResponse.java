package com.ecommerce.country.dto;

import com.ecommerce.common.AuditResponse;
import com.ecommerce.country.Country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponse {
    private Long id;
    private String code;
    private String name;
    private Boolean isActive;
    private AuditResponse audit;

    public static CountryResponse fromEntity(Country country) {
        return CountryResponse.builder()
                .id(country.getId())
                .code(country.getCode())
                .name(country.getName())
                .isActive(country.getIsActive())
                .audit(AuditResponse.fromAudit(country.getAudit()))
                .build();
    }
}
