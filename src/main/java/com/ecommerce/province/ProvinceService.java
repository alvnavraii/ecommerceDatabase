package com.ecommerce.province;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.ecommerce.common.Audit;
import com.ecommerce.common.exception.OracleException;
import com.ecommerce.province.dto.ProvinceRequest;
import com.ecommerce.province.dto.ProvinceResponse;
import com.ecommerce.autCom.AutComRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProvinceService {
    private final ProvinceRepository provinceRepository;
    private final AutComRepository autComRepository;

    public List<ProvinceResponse> getAllProvinces() {
        return provinceRepository.findAllByOrderByIdAsc().stream()
            .filter(Province::getIsActive)
            .sorted(Comparator.comparing(Province::getCode))
            .map(ProvinceResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public List<ProvinceResponse> getAllProvincesIncludingInactive() {
        return provinceRepository.findAllByOrderByIdAsc().stream()
            .sorted(Comparator.comparing(Province::getCode))
            .map(ProvinceResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public List<ProvinceResponse> getInactiveProvinces() {
        return provinceRepository.findAllByOrderByIdAsc().stream()
            .filter(province -> !province.getIsActive())
            .sorted(Comparator.comparing(Province::getCode))
                    .map(ProvinceResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public ProvinceResponse getProvinceById(Long id) {
        return provinceRepository.findById(id)
            .filter(Province::getIsActive)
            .map(ProvinceResponse::fromEntity)
            .orElseThrow(() -> new OracleException("1403", "No se han encontrado datos"));
    }

    @Transactional
    public ProvinceResponse createProvince(ProvinceRequest request) {
        var autCom = autComRepository.findById(request.getAutComId())
            .filter(ac -> ac.getIsActive())
            .orElseThrow(() -> new OracleException("1403", "No se han encontrado datos (autonomía no encontrada)"));

        // Validar que no exista una provincia con el mismo nombre
        if (provinceRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new OracleException("0001", "Unique constraint (PROVINCE.UK_PROVINCE_NAME) violated");
        }

        var audit = Audit.builder()
            .createdBy("system")
            .createdAt(LocalDateTime.now())
            .updatedBy("system")
            .updatedAt(LocalDateTime.now())
            .build();

        Province province = Province.builder()
            .code(request.getCode().toUpperCase())
            .alfaCode(request.getAlfaCode().toUpperCase())
            .name(request.getName())
            .autCom(autCom)
            .isActive(true)
            .audit(audit)
            .build();

        return ProvinceResponse.fromEntity(provinceRepository.save(province));
    }

    @Transactional
    public ProvinceResponse updateProvince(Long id, ProvinceRequest request) {
        Province province = provinceRepository.findById(id)
            .filter(Province::getIsActive)
            .orElseThrow(() -> new OracleException("1403", "No se han encontrado datos"));

        if (request.getCode() != null && !request.getCode().isBlank()) {
            province.setCode(request.getCode().toUpperCase());
        }
        
        if (request.getAlfaCode() != null && !request.getAlfaCode().isBlank()) {
            province.setAlfaCode(request.getAlfaCode().toUpperCase());
        }
        
        if (request.getName() != null && !request.getName().isBlank()) {
            province.setName(request.getName());
        }

        if (request.getAutComId() != null) {
            var autCom = autComRepository.findById(request.getAutComId())
                .filter(ac -> ac.getIsActive())
                .orElseThrow(() -> new OracleException("1403", "No se han encontrado datos (autonomía no encontrada)"));
            province.setAutCom(autCom);
        }

        province.getAudit().setUpdatedBy("system");
        province.getAudit().setUpdatedAt(LocalDateTime.now());

        return ProvinceResponse.fromEntity(provinceRepository.save(province));
    }

    @Transactional
    public ProvinceResponse deactivateProvince(Long id) {
        Province province = provinceRepository.findById(id)
            .filter(Province::getIsActive)
            .orElseThrow(() -> new OracleException("1403", "No se han encontrado datos"));

        province.setIsActive(false);
        province.getAudit().setUpdatedBy("system");
        province.getAudit().setUpdatedAt(LocalDateTime.now());

        return ProvinceResponse.fromEntity(provinceRepository.save(province));
    }

    @Transactional
    public ProvinceResponse reactivateProvince(Long id) {
        Province province = provinceRepository.findById(id)
            .filter(p -> !p.getIsActive())
            .orElseThrow(() -> new OracleException("1403", "No se han encontrado datos"));

        province.setIsActive(true);
        province.getAudit().setUpdatedBy("system");
        province.getAudit().setUpdatedAt(LocalDateTime.now());

        return ProvinceResponse.fromEntity(provinceRepository.save(province));
    }

    @Transactional
    public ProvinceResponse deleteProvince(Long id) {
       return deactivateProvince(id);
    }
}
