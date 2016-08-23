package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.FirstName;

@Converter
public class FirstNameConverter
		implements AttributeConverter<FirstName, String> {

	@Override
	public String convertToDatabaseColumn(FirstName value) {
		if (value == null) {
			return null;
		}
		return value.getName();
	}

	@Override
	public FirstName convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new FirstName(value);
	}

}
