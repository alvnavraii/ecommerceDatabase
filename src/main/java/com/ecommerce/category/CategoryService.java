package com.ecommerce.category;

import com.ecommerce.category.dto.CategoryResponse;
import com.ecommerce.category.dto.CreateCategoryRequest;
import com.ecommerce.category.dto.UpdateCategoryRequest;
import com.ecommerce.common.Audit;
import com.ecommerce.common.exception.OracleException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getRootCategories() {
        return categoryRepository.findRootCategories().stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryResponse> getAllRootCategoriesIncludingInactive() {
        return categoryRepository.findAllRootCategoriesIncludingInactive().stream()
                .map(CategoryResponse::fromEntityIncludingInactive)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryResponse::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    public Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    public CategoryResponse getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .map(CategoryResponse::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with slug: " + slug));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new OracleException("ORA-0001", 
                "Unique constraint (CATEGORIES.UK_CATEGORY_SLUG) violated");
        }

        if (request.getParentId() != null) {
            categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with id: " + request.getParentId()));
        }

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        Category category = Category.builder()
                .parentId(request.getParentId())
                .slug(request.getSlug())
                .isActive(true)
                .audit(Audit.builder()
                        .createdAt(now)
                        .updatedAt(now)
                        .createdBy(currentUser)
                        .updatedBy(currentUser)
                        .build())
                .build();

        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = findEntityById(id);
        if (!category.getIsActive()) {
            throw new OracleException("ORA-0002", "Category is inactive");
        }

        if (request.getSlug() != null) {
            if (!category.getSlug().equals(request.getSlug()) && categoryRepository.existsBySlug(request.getSlug())) {
                throw new OracleException("ORA-0001", "Unique constraint (CATEGORIES.UK_CATEGORY_SLUG) violated");
            }
            category.setSlug(request.getSlug());
        }

        // Permitimos explícitamente establecer parentId como null para desvincular
        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new OracleException("ORA-0003", "Category cannot be its own parent");
            }

            Category parent = findEntityById(request.getParentId());
            if (!parent.getIsActive()) {
                throw new OracleException("ORA-0003", "Cannot update category: parent category is inactive");
            }
        }
        category.setParentId(request.getParentId()); // Aquí permitimos null

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        category.getAudit().setUpdatedAt(now);
        category.getAudit().setUpdatedBy(username);

        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        // Desactivar recursivamente la categoría y sus hijos
        deactivateRecursively(category);

        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse reactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        if (category.getIsActive()) {
            throw new IllegalArgumentException("Category is already active");
        }

        // Si tiene padre, verificar que esté activo
        if (category.getParentId() != null) {
            Category parent = categoryRepository.findById(category.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found with id: " + category.getParentId()));
            
            if (!parent.getIsActive()) {
                throw new IllegalArgumentException("Cannot reactivate category because parent category is inactive");
            }
        }

        category.setIsActive(true);
        updateAudit(category);

        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    private void deactivateRecursively(Category category) {
        category.setIsActive(false);
        updateAudit(category);
        categoryRepository.save(category);

        // Desactivar recursivamente todos los hijos
        for (Category child : category.getChildren()) {
            deactivateRecursively(child);
        }
    }

    private void updateAudit(Category category) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Audit audit = category.getAudit();
        audit.setUpdatedAt(LocalDateTime.now());
        audit.setUpdatedBy(currentUser);
        category.setAudit(audit);
    }
}
