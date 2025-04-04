package com.ecommerce.category;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.ecommerce.common.Audit;

@Entity
@Table(name = "CATEGORIES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", insertable = false, updatable = false)
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    @Column(name = "SLUG", nullable = false, unique = true)
    private String slug;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Embedded
    private Audit audit;
}
