package com.ecommerce.address.dto;

import com.ecommerce.common.AuditResponse;
import com.ecommerce.address.Address;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private String address;
    private String postalCode;
    private Boolean isDefault;
    private Boolean isActive;
    private AuditResponse audit;
    private MunicipalityDTO municipality;

    public static AddressResponse fromEntity(Address entity) {
        return AddressResponse.builder()
                .id(entity.getId())
                .address(entity.getAddress())
                .municipality(MunicipalityDTO.fromEntity(entity.getMunicipality()))
                .postalCode(entity.getPostalCode())
                .isDefault(entity.getIsDefault())
                .isActive(entity.getIsActive())
                .audit(AuditResponse.fromAudit(entity.getAudit()))
                .build();
    }
}


