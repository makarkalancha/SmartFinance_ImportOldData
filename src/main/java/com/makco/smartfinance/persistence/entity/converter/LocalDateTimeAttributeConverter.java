package com.makco.smartfinance.persistence.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Makar Kalancha on 2016-04-19.
 */
//http://www.thoughts-on-java.org/jpa-21-how-to-implement-type-converter/
//http://www.thoughts-on-java.org/persist-localdate-localdatetime-jpa/
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        return (attribute == null) ? null : Timestamp.valueOf(attribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        return (dbData == null) ? null : dbData.toLocalDateTime();
    }
}
