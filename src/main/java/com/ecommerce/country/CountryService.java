package com.ecommerce.country;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.common.Audit;
import com.ecommerce.common.exception.OracleException;
import com.ecommerce.country.dto.CountryRequest;
import com.ecommerce.country.dto.CountryResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CountryService {
    private static final String COUNTRY_NOT_FOUND = "No se han encontrado datos (país no encontrado)";
    private static final String COUNTRY_CODE_EXISTS = "Violación de restricción única (COUNTRIES.UK_COUNTRY_CODE)";
    
    private final CountryRepository countryRepository;

    public List<CountryResponse> getAllCountries() {
        return countryRepository.findAllByOrderByIdAsc().stream()
                .filter(Country::getIsActive)
                .map(CountryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CountryResponse> getAllCountriesIncludingInactive() {
        return countryRepository.findAllByOrderByIdAsc().stream()
                .map(CountryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CountryResponse> getInactiveCountries() {
        return countryRepository.findAllByOrderByIdAsc().stream()
                .filter(country -> !country.getIsActive())
                .map(CountryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public CountryResponse getCountryById(Long id) {
        return countryRepository.findById(id)
                .filter(Country::getIsActive)
                .map(CountryResponse::fromEntity)
                .orElseThrow(() -> new OracleException("01403", COUNTRY_NOT_FOUND));
    }

    public CountryResponse getCountryByCode(String code) {
        return countryRepository.findByCodeAndIsActiveTrue(code.toUpperCase())
                .map(CountryResponse::fromEntity)
                .orElseThrow(() -> new OracleException("01403", COUNTRY_NOT_FOUND));
    }

    @Transactional
    public CountryResponse createCountry(CountryRequest request) {
        String code = request.getCode().toUpperCase();
        if (countryRepository.existsByCode(code)) {
            throw new OracleException("00001", COUNTRY_CODE_EXISTS);
        }

        LocalDateTime now = LocalDateTime.now();
        Audit audit = Audit.builder()
                .createdAt(now)
                .updatedAt(now)
                .createdBy("system")
                .updatedBy("system")
                .build();

        Country country = Country.builder()
                .code(code)
                .name(request.getName())
                .isActive(true)
                .audit(audit)
                .build();

        return CountryResponse.fromEntity(countryRepository.save(country));
    }

    @Transactional
    public CountryResponse updateCountry(Long id, CountryRequest request) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new OracleException("01403", COUNTRY_NOT_FOUND));

        String newCode = request.getCode().toUpperCase();
        if (!newCode.equals(country.getCode()) && countryRepository.existsByCode(newCode)) {
            throw new OracleException("00001", COUNTRY_CODE_EXISTS);
        }

        country.setCode(newCode);
        if (request.getName() != null && !request.getName().isBlank()) {
            country.setName(request.getName());
        }
        country.getAudit().setUpdatedBy("system");
        country.getAudit().setUpdatedAt(LocalDateTime.now());

        return CountryResponse.fromEntity(countryRepository.save(country));
    }

    @Transactional
    public CountryResponse deactivateCountry(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new OracleException("01403", COUNTRY_NOT_FOUND));

        country.setIsActive(false);
        country.getAudit().setUpdatedBy("system");
        country.getAudit().setUpdatedAt(LocalDateTime.now());
        
        return CountryResponse.fromEntity(countryRepository.save(country));
    }

    @Transactional
    public CountryResponse reactivateCountry(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new OracleException("01403", COUNTRY_NOT_FOUND));

        country.setIsActive(true);
        country.getAudit().setUpdatedBy("system");
        country.getAudit().setUpdatedAt(LocalDateTime.now());
        
        return CountryResponse.fromEntity(countryRepository.save(country));
    }

    @Transactional
    public CountryResponse deleteCountry(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new OracleException("01403", COUNTRY_NOT_FOUND));

        country.getAudit().setUpdatedBy("system");
        country.getAudit().setUpdatedAt(LocalDateTime.now());
        country.setIsActive(false);
        
        return CountryResponse.fromEntity(countryRepository.save(country));
    }
}
