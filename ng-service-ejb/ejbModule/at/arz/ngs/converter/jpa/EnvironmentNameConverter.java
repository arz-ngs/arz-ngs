package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.EnvironmentName;

@Converter
public class EnvironmentNameConverter
		implements AttributeConverter<EnvironmentName, String> {

	@Override
	public String convertToDatabaseColumn(EnvironmentName value) {
		if (value == null) {
			return null;
		}
		return value.getName();
	}

	@Override
	public EnvironmentName convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new EnvironmentName(value);
	}

}
