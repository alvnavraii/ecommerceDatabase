package com.ecommerce.language;

import com.ecommerce.common.Audit;
import com.ecommerce.common.exception.OracleException;
import com.ecommerce.language.dto.CreateLanguageRequest;
import com.ecommerce.language.dto.LanguageResponse;
import com.ecommerce.language.dto.UpdateLanguageRequest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    public List<LanguageResponse> getAllLanguages() {
        return languageRepository.findAll().stream()
                .map(LanguageResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<LanguageResponse> getAllLanguagesIncludingInactive() {
        return languageRepository.findAllIncludingInactive().stream()
                .map(LanguageResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public LanguageResponse getLanguageById(@NonNull Long id) {
        return languageRepository.findById(id)
                .filter(Language::getIsActive)
                .map(LanguageResponse::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with id: " + id));
    }

    public Language findEntityById(@NonNull Long id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with id: " + id));
    }

    public LanguageResponse getLanguageByCode(@NonNull String code) {
        return languageRepository.findByCodeAndIsActiveTrue(code.toUpperCase())
                .map(LanguageResponse::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with code: " + code));
    }

    @Transactional
    public LanguageResponse createLanguage(@NonNull CreateLanguageRequest request) {
        if (languageRepository.existsByCode(request.getCode())) {
            throw new OracleException("ORA-0001", 
                "Unique constraint (LANGUAGES.UK_LANGUAGE_CODE) violated");
        }

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        Language language = Language.builder()
                .code(request.getCode())
                .name(request.getName())
                .nativeName(request.getNativeName())
                .flagUrl(request.getFlagUrl())
                .isActive(true)
                .audit(Audit.builder()
                        .createdAt(now)
                        .updatedAt(now)
                        .createdBy(currentUser)
                        .updatedBy(currentUser)
                        .build())
                .build();

        return LanguageResponse.fromEntity(languageRepository.save(language));
    }

    @Transactional
    public LanguageResponse updateLanguage(@NonNull Long id, @NonNull UpdateLanguageRequest request) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with id: " + id));

        if (request.getCode() != null && !request.getCode().equals(language.getCode())) {
            if (languageRepository.existsByCode(request.getCode())) {
                throw new OracleException("ORA-0001", 
                    "Unique constraint (LANGUAGES.UK_LANGUAGE_CODE) violated");
            }
            language.setCode(request.getCode());
        }

        if (request.getName() != null) {
            language.setName(request.getName());
        }
        
        if (request.getNativeName() != null) {
            language.setNativeName(request.getNativeName());
        }
        
        if (request.getFlagUrl() != null) {
            language.setFlagUrl(request.getFlagUrl());
        }

        if (request.getIsActive() != null) {
            language.setIsActive(request.getIsActive());
        }

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Audit audit = language.getAudit();
        audit.setUpdatedAt(LocalDateTime.now());
        audit.setUpdatedBy(currentUser);
        language.setAudit(audit);

        return LanguageResponse.fromEntity(languageRepository.save(language));
    }

    @Transactional
    public LanguageResponse deleteLanguage(@NonNull Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with id: " + id));

        language.setIsActive(false);

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Audit audit = language.getAudit();
        audit.setUpdatedAt(LocalDateTime.now());
        audit.setUpdatedBy(currentUser);
        language.setAudit(audit);

        language = languageRepository.save(language);
        return LanguageResponse.fromEntity(language);
    }

    @Transactional
    public LanguageResponse reactivateLanguage(@NonNull Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with id: " + id));

        if (language.getIsActive()) {
            throw new IllegalArgumentException("Language is already active");
        }

        language.setIsActive(true);

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Audit audit = language.getAudit();
        audit.setUpdatedAt(LocalDateTime.now());
        audit.setUpdatedBy(currentUser);
        language.setAudit(audit);

        language = languageRepository.save(language);
        return LanguageResponse.fromEntity(language);
    }
}
