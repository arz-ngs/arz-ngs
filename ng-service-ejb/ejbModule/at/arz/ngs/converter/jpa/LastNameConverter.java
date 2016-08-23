package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.LastName;

@Converter
public class LastNameConverter
		implements AttributeConverter<LastName, String> {

	@Override
	public String convertToDatabaseColumn(LastName value) {
		if (value == null) {
			return null;
		}
		return value.getName();
	}

	@Override
	public LastName convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new LastName(value);
	}

}
