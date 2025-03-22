package com.ecommerce.categoryTranslation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTranslationId implements Serializable {
    private Long category;
    private Long language;
}
