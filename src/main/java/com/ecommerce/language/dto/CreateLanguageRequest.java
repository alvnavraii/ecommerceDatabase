package com.ecommerce.language.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLanguageRequest {
    private String code;
    private String name;
    private String nativeName;
    private String flagUrl;
}
