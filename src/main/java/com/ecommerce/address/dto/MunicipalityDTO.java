package com.ecommerce.address.dto;

import com.ecommerce.municipality.Municipality;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MunicipalityDTO {
    private Long id;
    private String name;
    private ProvinceDto province;

    public static MunicipalityDTO fromEntity(Municipality municipality) {
        return MunicipalityDTO.builder()
                .id(municipality.getId())
                .name(municipality.getName())
                .province(ProvinceDto.fromEntity(municipality.getProvince()))
                .build();
    }
} 