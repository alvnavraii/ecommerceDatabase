package com.ecommerce.measurement_unit_translation;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.ecommerce.measurement_unit_translation.dto.MeasurementUnitTranslationRequest;
import com.ecommerce.measurement_unit_translation.dto.MeasurementUnitTranslationResponse;
import com.ecommerce.common.exception.OracleException;
import com.ecommerce.common.Audit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ecommerce.measurementUnit.MeasurementUnitRepository;
import com.ecommerce.language.LanguageRepository;

@Service
@RequiredArgsConstructor
public class MeasurementUnitTranslationService {
    private final MeasurementUnitTranslationRepository measurementunittranslationRepository;
    private final MeasurementUnitRepository measurementUnitRepository;
    private final LanguageRepository languageRepository;

    public List<MeasurementUnitTranslationResponse> getAllMeasurementUnitTranslations() {
        return measurementunittranslationRepository.findAll().stream()
                .map(MeasurementUnitTranslationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MeasurementUnitTranslationResponse> getAllMeasurementUnitTranslationsIncludingInactive() {
        return measurementunittranslationRepository.findAllIncludingInactive().stream()
                .map(MeasurementUnitTranslationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MeasurementUnitTranslationResponse> getInactiveMeasurementUnitTranslations() {
        return measurementunittranslationRepository.findAllInactive().stream()
                .map(MeasurementUnitTranslationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public MeasurementUnitTranslationResponse getMeasurementUnitTranslationById(Long id) {
        return MeasurementUnitTranslationResponse.fromEntity(
            measurementunittranslationRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"))
        );
    }

    public MeasurementUnitTranslationResponse createMeasurementUnitTranslation(MeasurementUnitTranslationRequest request) {
        var measurementUnit = measurementUnitRepository.findById(request.getMeasurementUnitId())
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        var language = languageRepository.findById(request.getLanguageId())
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        var audit = Audit.builder()
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedBy("system")
                .updatedAt(LocalDateTime.now())
                .build();

        var measurementunittranslation = MeasurementUnitTranslation.builder()
                .name(request.getName())
                .measurementUnit(measurementUnit)
                .language(language)
                .isActive(true)
                .audit(audit)
                .build();

        return MeasurementUnitTranslationResponse.fromEntity(
            measurementunittranslationRepository.save(measurementunittranslation));
    }

    public MeasurementUnitTranslationResponse updateMeasurementUnitTranslation(Long id, MeasurementUnitTranslationRequest request) {
        var measurementunittranslation = measurementunittranslationRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        if (request.getMeasurementUnitId() != null) {
            measurementunittranslation.setMeasurementUnit(measurementUnitRepository.findById(request.getMeasurementUnitId())
                    .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos")));
        }
        
        if (request.getLanguageId() != null) {
            measurementunittranslation.setLanguage(languageRepository.findById(request.getLanguageId())
                    .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos")));
        }
        
        if (request.getName() != null && !request.getName().isBlank()) {
            measurementunittranslation.setName(request.getName());
        }

        if (request.getIsActive() != null) {
            measurementunittranslation.setIsActive(request.getIsActive());
        }

        measurementunittranslation.getAudit().setUpdatedBy("system");
        measurementunittranslation.getAudit().setUpdatedAt(LocalDateTime.now());

        return MeasurementUnitTranslationResponse.fromEntity(measurementunittranslationRepository.save(measurementunittranslation));
    }

    public MeasurementUnitTranslationResponse deactivateMeasurementUnitTranslation(Long id) {
        var measurementunittranslation = measurementunittranslationRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        measurementunittranslation.setIsActive(false);
        measurementunittranslation.getAudit().setUpdatedBy("system");
        measurementunittranslation.getAudit().setUpdatedAt(LocalDateTime.now());

        return MeasurementUnitTranslationResponse.fromEntity(measurementunittranslationRepository.save(measurementunittranslation));
    }

    public MeasurementUnitTranslationResponse reactivateMeasurementUnitTranslation(Long id) {
        var measurementunittranslation = measurementunittranslationRepository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        measurementunittranslation.setIsActive(true);
        measurementunittranslation.getAudit().setUpdatedBy("system");
        measurementunittranslation.getAudit().setUpdatedAt(LocalDateTime.now());

        return MeasurementUnitTranslationResponse.fromEntity(measurementunittranslationRepository.save(measurementunittranslation));
    }

    public MeasurementUnitTranslationResponse deleteMeasurementUnitTranslation(Long id) {
        return deactivateMeasurementUnitTranslation(id);
    }
}
