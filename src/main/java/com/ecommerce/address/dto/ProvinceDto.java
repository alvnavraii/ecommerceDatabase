package com.ecommerce.address.dto;

import com.ecommerce.province.Province;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProvinceDto {
    private Long id;
    private String name;

    public static ProvinceDto fromEntity(Province province) {
        return ProvinceDto.builder()
                .id(province.getId())
                .name(province.getName())
                .build();
    }
}
