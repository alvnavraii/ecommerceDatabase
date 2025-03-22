package com.ecommerce.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter
public class OracleBooleanConverter implements AttributeConverter<Boolean, BigDecimal> {
    private static final BigDecimal TRUE_VALUE = BigDecimal.ONE;
    private static final BigDecimal FALSE_VALUE = BigDecimal.ZERO;

    @Override
    public BigDecimal convertToDatabaseColumn(Boolean attribute) {
        return Boolean.TRUE.equals(attribute) ? TRUE_VALUE : FALSE_VALUE;
    }

    @Override
    public Boolean convertToEntityAttribute(BigDecimal dbData) {
        if (dbData == null) {
            return Boolean.FALSE;
        }
        return TRUE_VALUE.equals(dbData);
    }
}
