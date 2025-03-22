package com.ecommerce.municipality;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.ecommerce.municipality.dto.MunicipalityRequest;
import com.ecommerce.municipality.dto.MunicipalityResponse;
import com.ecommerce.common.exception.OracleException;
import com.ecommerce.common.Audit;
import com.ecommerce.province.Province;
import com.ecommerce.province.ProvinceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MunicipalityService {
    private final MunicipalityRepository municipalityRepository;
    private final ProvinceRepository provinceRepository;

    public List<MunicipalityResponse> getAllMunicipalitys() {
        return municipalityRepository.findAll().stream()
                .map(MunicipalityResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MunicipalityResponse> getAllMunicipalitysIncludingInactive() {
        return municipalityRepository.findAllIncludingInactive().stream()
                .map(MunicipalityResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MunicipalityResponse> getInactiveMunicipalitys() {
        return municipalityRepository.findAllInactive().stream()
                .map(MunicipalityResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public MunicipalityResponse getMunicipalityById(Long id) {
        return MunicipalityResponse.fromEntity(
            municipalityRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"))
        );
    }

    public MunicipalityResponse getMunicipalityByCode(String code) {
        return MunicipalityResponse.fromEntity(
            municipalityRepository.findByCode(code)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"))
        );
    }   

    public MunicipalityResponse createMunicipality(MunicipalityRequest request) {
        Province province = provinceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new OracleException("ORA-01403", "No se ha encontrado la provincia"));

        var audit = Audit.builder()
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedBy("system")
                .updatedAt(LocalDateTime.now())
                .build();

        var municipality = Municipality.builder()
                .code(request.getCode())
                .name(request.getName())
                .province(province)
                .isActive(true)
                .audit(audit)
                .build();

        return MunicipalityResponse.fromEntity(municipalityRepository.save(municipality));
    }

    public MunicipalityResponse updateMunicipality(Long id, MunicipalityRequest request) {
        var municipality = municipalityRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        if (request.getProvinceId() != null) {
            Province province = provinceRepository.findById(request.getProvinceId())
                    .orElseThrow(() -> new OracleException("ORA-01403", "No se ha encontrado la provincia"));
            municipality.setProvince(province);
        }

        if (request.getCode() != null) {
            municipality.setCode(request.getCode());
        }

        if (request.getName() != null) {
            municipality.setName(request.getName());
        }

        municipality.getAudit().setUpdatedBy("system");
        municipality.getAudit().setUpdatedAt(LocalDateTime.now());

        return MunicipalityResponse.fromEntity(municipalityRepository.save(municipality));
    }

    public MunicipalityResponse deactivateMunicipality(Long id) {
        var municipality = municipalityRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        municipality.setIsActive(false);
        municipality.getAudit().setUpdatedBy("system");
        municipality.getAudit().setUpdatedAt(LocalDateTime.now());

        return MunicipalityResponse.fromEntity(municipalityRepository.save(municipality));
    }

    public MunicipalityResponse reactivateMunicipality(Long id) {
        var municipality = municipalityRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        municipality.setIsActive(true);
        municipality.getAudit().setUpdatedBy("system");
        municipality.getAudit().setUpdatedAt(LocalDateTime.now());

        return MunicipalityResponse.fromEntity(municipalityRepository.save(municipality));
    }

    public MunicipalityResponse deleteMunicipality(Long id) {
        var municipality = municipalityRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        return this.deactivateMunicipality(municipality.getId());
    }
}
