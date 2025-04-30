package com.ecommerce.category;

import com.ecommerce.category.dto.CategoryResponse;
import com.ecommerce.category.dto.CreateCategoryRequest;
import com.ecommerce.category.dto.UpdateCategoryRequest;
import com.ecommerce.common.Audit;
import com.ecommerce.common.exception.OracleException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getRootCategories() {
        return categoryRepository.findRootCategories().stream()
                .map(category -> {
                    // Inicializar la colección children explícitamente
                    Hibernate.initialize(category.getChildren());
                    return CategoryResponse.fromEntity(category);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryResponse> getAllRootCategoriesIncludingInactive() {
        return categoryRepository.findAllRootCategoriesIncludingInactive().stream()
                .map(category -> {
                    // Inicializar la colección children explícitamente
                    Hibernate.initialize(category.getChildren());
                    return CategoryResponse.fromEntityIncludingInactive(category);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        
        // Inicializar la colección children explícitamente
        Hibernate.initialize(category.getChildren());
        
        return CategoryResponse.fromEntity(category);
    }

    @Transactional(readOnly = true)
    public Category findEntityById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        
        // Inicializar la colección children explícitamente
        Hibernate.initialize(category.getChildren());
        
        return category;
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with slug: " + slug));
        
        // Inicializar la colección children explícitamente
        Hibernate.initialize(category.getChildren());
        
        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        System.out.println("Iniciando creación de categoría");
        System.out.println("Datos de la solicitud: " + request.toString());

        if (categoryRepository.existsBySlug(request.getSlug())) {
            System.out.println("Error: El slug ya existe");
            throw new OracleException("ORA-0001", 
                "Unique constraint (CATEGORIES.UK_CATEGORY_SLUG) violated");
        }

        if (request.getParentId() != null) {
            System.out.println("Verificando existencia de categoría padre con ID: " + request.getParentId());
            categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with id: " + request.getParentId()));
        }

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        Category category = Category.builder()
                .parentId(request.getParentId())
                .slug(request.getSlug())
                .name(request.getName())
                .description(request.getDescription())
                .isActive(true)
                .audit(Audit.builder()
                        .createdAt(now)
                        .updatedAt(now)
                        .createdBy(currentUser)
                        .updatedBy(currentUser)
                        .build())
                .build();

        System.out.println("Guardando nueva categoría: " + category.toString());
        category = categoryRepository.save(category);
        
        System.out.println("Categoría guardada: " + category.toString());
        
        // Inicializar la colección children explícitamente para el objeto recién creado
        Hibernate.initialize(category.getChildren());
        
        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        System.out.println("Iniciando actualización de categoría con ID: " + id);
        System.out.println("Datos de la solicitud: " + request.toString());
        
        Category category = findEntityById(id);
        System.out.println("Categoría encontrada: " + category.toString());
        System.out.println("Estado actual - isActive: " + category.getIsActive());
        
        if (!category.getIsActive()) {
            System.out.println("Error: La categoría está inactiva");
            throw new OracleException("ORA-0002", "Category is inactive");
        }

        // El slug es obligatorio
        if (request.getSlug() != null) {
            System.out.println("Actualizando slug de: " + category.getSlug() + " a: " + request.getSlug());
            if (!category.getSlug().equals(request.getSlug()) && categoryRepository.existsBySlug(request.getSlug())) {
                System.out.println("Error: El slug ya existe");
                throw new OracleException("ORA-0001", "Unique constraint (CATEGORIES.UK_CATEGORY_SLUG) violated");
            }
            category.setSlug(request.getSlug());
        }
        
        // Campos opcionales - solo actualizar si se proporcionan
        if (request.getDescription() != null) {
            System.out.println("Actualizando descripción de: " + category.getDescription() + " a: " + request.getDescription());
            category.setDescription(request.getDescription());
        } else {
            System.out.println("No se proporcionó descripción en la solicitud");
        }
        
        if (request.getName() != null) {
            System.out.println("Actualizando nombre de: " + category.getName() + " a: " + request.getName());
            category.setName(request.getName());
        } else {
            System.out.println("No se proporcionó nombre en la solicitud");
        }

        // parentId es opcional - puede ser null para desvincular
        if (request.getParentId() != null) {
            System.out.println("Actualizando parentId de: " + category.getParentId() + " a: " + request.getParentId());
            if (request.getParentId().equals(id)) {
                System.out.println("Error: La categoría no puede ser su propio padre");
                throw new OracleException("ORA-0003", "Category cannot be its own parent");
            }

            Category parent = findEntityById(request.getParentId());
            if (!parent.getIsActive()) {
                System.out.println("Error: La categoría padre está inactiva");
                throw new OracleException("ORA-0003", "Cannot update category: parent category is inactive");
            }
        } else {
            System.out.println("No se proporcionó parentId en la solicitud o se estableció explícitamente como null");
        }
        category.setParentId(request.getParentId()); // Aquí permitimos null

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        System.out.println("Actualizando audit con usuario: " + username);
        category.getAudit().setUpdatedAt(now);
        category.getAudit().setUpdatedBy(username);
        
        System.out.println("Guardando categoría actualizada");
        Category savedCategory = categoryRepository.save(category);
        
        System.out.println("Categoría guardada: " + savedCategory.toString());
        
        // Inicializar la colección children explícitamente después de actualizar
        Hibernate.initialize(savedCategory.getChildren());
        
        return CategoryResponse.fromEntity(savedCategory);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        
        // Inicializar la colección children explícitamente
        Hibernate.initialize(category.getChildren());

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
        
        final Category updatedCategory = categoryRepository.save(category);
        
        // Inicializar la colección children explícitamente
        Hibernate.initialize(updatedCategory.getChildren());

        return CategoryResponse.fromEntity(category);
    }

    private void deactivateRecursively(Category category) {
        category.setIsActive(false);
        updateAudit(category);
        categoryRepository.save(category);

        // Desactivar recursivamente todos los hijos - asegurarse de que la colección esté inicializada
        if (Hibernate.isInitialized(category.getChildren())) {
            for (Category child : category.getChildren()) {
                deactivateRecursively(child);
            }
        } else {
            // Si no está inicializada, buscar los hijos por parentId
            List<Category> children = categoryRepository.findAll().stream()
                    .filter(c -> category.getId().equals(c.getParentId()))
                    .collect(Collectors.toList());
            
            for (Category child : children) {
                deactivateRecursively(child);
            }
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