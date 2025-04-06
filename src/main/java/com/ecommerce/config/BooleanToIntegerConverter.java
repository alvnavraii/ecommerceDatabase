package com.ecommerce.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// Desactivamos la conversión automática porque ya no la necesitamos
@Converter(autoApply = false)
public class BooleanToIntegerConverter implements AttributeConverter<Boolean, Boolean> {

    @Override
    public Boolean convertToDatabaseColumn(Boolean attribute) {
        // Simplemente pasar el valor booleano sin conversión
        return attribute;
    }

    @Override
    public Boolean convertToEntityAttribute(Boolean dbData) {
        // Simplemente pasar el valor booleano sin conversión
        return dbData;
    }
} 