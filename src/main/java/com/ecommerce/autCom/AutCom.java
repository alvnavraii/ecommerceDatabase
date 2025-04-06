/*ame       Null?    Type          
---------- -------- ------------- 
id         NOT NULL NUMBER        
country_id NOT NULL NUMBER        
code       NOT NULL VARCHAR2(4)   
name       NOT NULL VARCHAR2(100) 
is_active  NOT NULL NUMBER(1)     
created_at NOT NULL TIMESTAMP(6)  
updated_at NOT NULL TIMESTAMP(6)  
created_by NOT NULL VARCHAR2(50)  
updated_by NOT NULL VARCHAR2(50)  */

package com.ecommerce.autCom;

import com.ecommerce.common.Audit;
import com.ecommerce.country.Country;
import com.ecommerce.province.Province;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.SequenceGenerator;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "autonomous_communities",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_aut_com_code", columnNames = "code")
       })
public class AutCom {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aut_com_seq")
    @SequenceGenerator(name = "aut_com_seq", sequenceName = "aut_com_seq", allocationSize = 1)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "alfa_code", nullable = false)
    private String alfaCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "autCom")
    @Builder.Default
    private List<Province> provinces = new ArrayList<>();

    @Embedded
    private Audit audit;
}
