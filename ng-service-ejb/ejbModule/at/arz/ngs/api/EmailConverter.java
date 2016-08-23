package at.arz.ngs.api;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EmailConverter
		implements AttributeConverter<Email, String> {

	@Override
	public String convertToDatabaseColumn(Email value) {
		if (value == null) {
			return null;
		}
		return value.getEmail();
	}

	@Override
	public Email convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new Email(value);
	}

}
