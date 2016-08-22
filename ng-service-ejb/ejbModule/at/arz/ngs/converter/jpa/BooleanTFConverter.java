package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanTFConverter
		implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean value) {
		if (Boolean.TRUE.equals(value)) {
			return "true";
		} else {
			return "false";
		}
	}

	@Override
	public Boolean convertToEntityAttribute(String value) {
		return "true".equals(value);
	}
}