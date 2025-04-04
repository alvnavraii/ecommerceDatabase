package com.ecommerce.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BooleanToIntegerConverter implements AttributeConverter<Boolean, Character> {

    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? 't' : 'f';
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        return dbData != null && dbData.equals('t');
    }
} 