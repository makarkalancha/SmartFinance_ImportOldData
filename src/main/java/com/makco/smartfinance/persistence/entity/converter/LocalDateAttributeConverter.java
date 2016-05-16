package com.makco.smartfinance.persistence.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by mcalancea on 2016-04-19.
 */
//http://www.thoughts-on-java.org/jpa-21-how-to-implement-type-converter/
//http://www.thoughts-on-java.org/persist-localdate-localdatetime-jpa/
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        return (attribute == null) ? null : Date.valueOf(attribute);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        return (dbData == null) ? null : dbData.toLocalDate();
    }
}
