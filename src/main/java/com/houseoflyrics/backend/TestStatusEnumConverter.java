package com.houseoflyrics.backend;

import com.houseoflyrics.backend.entity.TestStatus.TestStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TestStatusEnumConverter implements AttributeConverter<TestStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(TestStatusEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public TestStatusEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (TestStatusEnum status : TestStatusEnum.values()) {
            if (status.getValue().equals(dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
