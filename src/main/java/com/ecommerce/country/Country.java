/*Name       Null?    Type          
---------- -------- ------------- 
ID         NOT NULL NUMBER        
CODE       NOT NULL VARCHAR2(2)   
NAME       NOT NULL VARCHAR2(100) 
IS_ACTIVE  NOT NULL NUMBER(1)     
CREATED_AT NOT NULL TIMESTAMP(6)  
UPDATED_AT NOT NULL TIMESTAMP(6)  
CREATED_BY NOT NULL VARCHAR2(50)  
UPDATED_BY NOT NULL VARCHAR2(50)  
 */
package com.ecommerce.country;

import com.ecommerce.common.Audit;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COUNTRIES", schema = "ecommerce",
       uniqueConstraints = {
           @UniqueConstraint(name = "UK_COUNTRY_CODE", columnNames = "CODE")
       })
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Embedded
    private Audit audit;
}
