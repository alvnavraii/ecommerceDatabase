package com.ecommerce.address;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.ecommerce.address.dto.AddressRequest;
import com.ecommerce.address.dto.AddressResponse;
import com.ecommerce.common.exception.OracleException;
import com.ecommerce.municipality.MunicipalityRepository;
import com.ecommerce.common.Audit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final MunicipalityRepository municipalityRepository;
    public List<AddressResponse> getAllAddresss() {
        return addressRepository.findAll().stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AddressResponse> getAllAddresssIncludingInactive() {
        return addressRepository.findAllIncludingInactive().stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AddressResponse> getInactiveAddresss() {
        return addressRepository.findAllInactive().stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public AddressResponse getAddressById(Long id) {
        return AddressResponse.fromEntity(
            addressRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"))
        );
    }

    public AddressResponse createAddress(AddressRequest request) {
        var audit = Audit.builder()
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedBy("system")
                .updatedAt(LocalDateTime.now())
                .build();

        var address = Address.builder()
                .userId(request.getUserId())
                .address(request.getAddress())
                .postalCode(request.getPostalCode())
                .isDefault(request.getIsDefault())
                .isActive(true)
                .audit(audit)
                .build();

        return AddressResponse.fromEntity(addressRepository.save(address));
    }

    public AddressResponse updateAddress(Long id, AddressRequest request) {
        var address = addressRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        if (request.getAddress() != null && !request.getAddress().isBlank()) {
            address.setAddress(request.getAddress());
        }

        if (request.getPostalCode() != null && !request.getPostalCode().isBlank()) {
            address.setPostalCode(request.getPostalCode());
        }

        if (request.getIsDefault() != null) {
            address.setIsDefault(request.getIsDefault());
        }

        if (request.getMunicipalityId() != null) {
            var municipality = municipalityRepository.findById(request.getMunicipalityId())
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));
            address.setMunicipality(municipality);
        }

        address.getAudit().setUpdatedBy("system");
        address.getAudit().setUpdatedAt(LocalDateTime.now());

        return AddressResponse.fromEntity(addressRepository.save(address));
    }

    public AddressResponse deactivateAddress(Long id) {
        var address = addressRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        address.setIsActive(false);
        address.getAudit().setUpdatedBy("system");
        address.getAudit().setUpdatedAt(LocalDateTime.now());

        return AddressResponse.fromEntity(addressRepository.save(address));
    }

    public AddressResponse reactivateAddress(Long id) {
        var address = addressRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        address.setIsActive(true);
        address.getAudit().setUpdatedBy("system");
        address.getAudit().setUpdatedAt(LocalDateTime.now());

        return AddressResponse.fromEntity(addressRepository.save(address));
    }

    public AddressResponse deleteAddress(Long id) {
        return deactivateAddress(id);
    }
}
