package com.ecommerce.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = false)
public class OracleNumberToBooleanConverter implements AttributeConverter<Boolean, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? BigDecimal.ONE : BigDecimal.ZERO;
    }

    @Override
    public Boolean convertToEntityAttribute(BigDecimal dbData) {
        return dbData != null && BigDecimal.ONE.compareTo(dbData) == 0;
    }
}
