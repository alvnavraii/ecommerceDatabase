package com.ecommerce.language;

import com.ecommerce.common.Audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LANGUAGES", 
       uniqueConstraints = {
           @UniqueConstraint(name = "UK_LANGUAGE_CODE", columnNames = "CODE")
       })
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "language_seq")
    @SequenceGenerator(name = "language_seq", sequenceName = "language_seq", allocationSize = 1)
    private Long id;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "NAME", nullable = false)
    private String name;
    
    @Column(name = "NATIVE_NAME")
    private String nativeName;
    
    @Column(name = "FLAG_URL")
    private String flagUrl;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Embedded
    private Audit audit;
}
