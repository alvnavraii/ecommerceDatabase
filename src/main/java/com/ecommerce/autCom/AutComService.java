package com.ecommerce.autCom;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.lang.NonNull;

import com.ecommerce.common.Audit;
import com.ecommerce.common.exception.OracleException;
import com.ecommerce.autCom.dto.AutComRequest;
import com.ecommerce.autCom.dto.AutComResponse;
import com.ecommerce.country.CountryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AutComService {
    private static final String AUT_COM_NOT_FOUND = "No se han encontrado datos (autonomía no encontrada)";
    private static final String AUT_COM_CODE_EXISTS = "Violación de restricción única (AUT_COM.UK_AUT_COM_CODE)";
    private static final String COUNTRY_NOT_FOUND = "No se han encontrado datos (país no encontrado)";

    private final AutComRepository autComRepository;
    private final CountryRepository countryRepository;

    public List<AutComResponse> getAllAutComs() {
        var autComs = autComRepository.findAllByOrderByIdAsc().stream()
                .filter(AutCom::getIsActive)
                .map(AutComResponse::fromEntity)
                .collect(Collectors.toList());
        autComs.sort(Comparator.comparing(AutComResponse::getCode));
        return autComs;
    }

    public List<AutComResponse> getAllAutComsIncludingInactive() {
        var autComs = autComRepository.findAllByOrderByIdAsc().stream()
                .map(AutComResponse::fromEntity)
                .collect(Collectors.toList());
        autComs.sort(Comparator.comparing(AutComResponse::getCode));
        return autComs;
    }

    public List<AutComResponse> getInactiveAutComs() {
        var autComs = autComRepository.findAllByOrderByIdAsc().stream()
                .filter(autCom -> !autCom.getIsActive())
                .map(AutComResponse::fromEntity)
                .collect(Collectors.toList());
        autComs.sort(Comparator.comparing(AutComResponse::getCode));
        return autComs;
    }

    public AutComResponse getAutComById(@NonNull Long id) {
        return autComRepository.findById(id)
                .filter(AutCom::getIsActive)
                .map(AutComResponse::fromEntity)
                .orElseThrow(() -> new OracleException("01403", AUT_COM_NOT_FOUND));
    }

    public AutComResponse getAutComByCode(@NonNull String code) {
        return autComRepository.findByCodeAndIsActiveTrue(code.toUpperCase())
                .map(AutComResponse::fromEntity)
                .orElseThrow(() -> new OracleException("01403", AUT_COM_NOT_FOUND));
    }

    @Transactional
    public AutComResponse createAutCom(@NonNull AutComRequest request) {
        // Validar campos obligatorios
        if (request.getCode() == null || request.getCode().isBlank()) {
            throw new OracleException("01400", "El código de la comunidad autónoma es obligatorio");
        }
        if (request.getAlfaCode() == null || request.getAlfaCode().isBlank()) {
            throw new OracleException("01400", "El código alfa de la comunidad autónoma es obligatorio");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new OracleException("01400", "El nombre de la comunidad autónoma es obligatorio");
        }
        if (request.getCountryId() == null) {
            throw new OracleException("01400", "El ID del país es obligatorio");
        }

        String code = request.getCode().toUpperCase();
        String alfaCode = request.getAlfaCode().toUpperCase();

        if (autComRepository.existsByCode(code)) {
            throw new OracleException("00001", AUT_COM_CODE_EXISTS);
        }

        var country = countryRepository.findById(request.getCountryId())
            .filter(c -> c.getIsActive())
            .orElseThrow(() -> new OracleException("01403", COUNTRY_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        Audit audit = Audit.builder()
                .createdAt(now)
                .updatedAt(now)
                .createdBy("system")
                .updatedBy("system")
                .build();

        AutCom autCom = AutCom.builder()
                .code(code)
                .alfaCode(alfaCode)
                .name(request.getName())
                .country(country)
                .isActive(true)
                .audit(audit)
                .build();

        return AutComResponse.fromEntity(autComRepository.save(autCom));
    }

    @Transactional
    public AutComResponse updateAutCom(@NonNull Long id, @NonNull AutComRequest request) {
        AutCom autCom = autComRepository.findById(id)
                .filter(AutCom::getIsActive)
                .orElseThrow(() -> new OracleException("01403", AUT_COM_NOT_FOUND));

        // Solo actualizar código si se proporciona y es diferente
        if (request.getCode() != null && !request.getCode().isBlank()) {
            String newCode = request.getCode().toUpperCase();
            if (!newCode.equals(autCom.getCode()) && autComRepository.existsByCode(newCode)) {
                throw new OracleException("00001", AUT_COM_CODE_EXISTS);
            }
            autCom.setCode(newCode);
        }

        // Solo actualizar alfaCode si se proporciona
        if (request.getAlfaCode() != null && !request.getAlfaCode().isBlank()) {
            autCom.setAlfaCode(request.getAlfaCode().toUpperCase());
        }

        // Solo actualizar nombre si se proporciona
        if (request.getName() != null && !request.getName().isBlank()) {
            autCom.setName(request.getName());
        }

        // Solo actualizar país si se proporciona
        if (request.getCountryId() != null) {
            var country = countryRepository.findById(request.getCountryId())
                .filter(c -> c.getIsActive())
                .orElseThrow(() -> new OracleException("01403", COUNTRY_NOT_FOUND));
            autCom.setCountry(country);
        }

        autCom.getAudit().setUpdatedBy("system");
        autCom.getAudit().setUpdatedAt(LocalDateTime.now());

        return AutComResponse.fromEntity(autComRepository.save(autCom));
    }

    @Transactional
    public AutComResponse deactivateAutCom(@NonNull Long id) {
        AutCom autCom = autComRepository.findById(id)
                .filter(AutCom::getIsActive)
                .orElseThrow(() -> new OracleException("01403", AUT_COM_NOT_FOUND));

        autCom.setIsActive(false);
        autCom.getAudit().setUpdatedBy("system");
        autCom.getAudit().setUpdatedAt(LocalDateTime.now());

        return AutComResponse.fromEntity(autComRepository.save(autCom));
    }

    @Transactional
    public AutComResponse reactivateAutCom(@NonNull Long id) {
        AutCom autCom = autComRepository.findById(id)
                .filter(ac -> !ac.getIsActive())
                .orElseThrow(() -> new OracleException("01403", AUT_COM_NOT_FOUND));

        // Validate that the country is active before reactivating the AutCom
        if (!autCom.getCountry().getIsActive()) {
            throw new OracleException("02291", "Violación de integridad referencial (el país está inactivo)");
        }

        autCom.setIsActive(true);
        autCom.getAudit().setUpdatedBy("system");
        autCom.getAudit().setUpdatedAt(LocalDateTime.now());

        return AutComResponse.fromEntity(autComRepository.save(autCom));
    }

    @Transactional
    public AutComResponse deleteAutCom(@NonNull Long id) {
        return deactivateAutCom(id);
    }
}