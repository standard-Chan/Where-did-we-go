package com.wheredidwego.util;

import com.wheredidwego.domain.enumerate.TouristCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
//ENUM 을 DB에 저장할 때 사용
public class TouristCategoryConverter implements AttributeConverter<TouristCategory, String> {
    // JPA에서 DB로 데이터를 저장할 때 자동 호출
    public String convertToDatabaseColumn(TouristCategory attribute) {
        return attribute.getCode();
    }

    // DB에서 JPA로 데이터를 가져올때 자동 호출
    public TouristCategory convertToEntityAttribute(String dbData) {
        return TouristCategory.getCategoryFromCode(dbData);
    }
}

