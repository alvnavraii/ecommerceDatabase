package com.ecommerce.province.dto;

import com.ecommerce.province.Province;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceBasicResponse {
    private Long id;
    private String code;
    private String alfaCode;
    private String name;
    private Boolean isActive;

    public static ProvinceBasicResponse fromEntity(Province province) {
        return ProvinceBasicResponse.builder()
                .id(province.getId())
                .code(province.getCode())
                .alfaCode(province.getAlfaCode())
                .name(province.getName())
                .isActive(province.getIsActive())
                .build();
    }
}
