package com.houseoflyrics.backend;

import com.houseoflyrics.backend.entity.DictationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DictationStatusEnumConverter implements AttributeConverter<DictationStatus.DictationStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(DictationStatus.DictationStatusEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public DictationStatus.DictationStatusEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (DictationStatus.DictationStatusEnum status : DictationStatus.DictationStatusEnum.values()) {
            if (status.getValue().equals(dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
