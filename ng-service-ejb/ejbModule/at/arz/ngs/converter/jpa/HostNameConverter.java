package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.HostName;

@Converter
public class HostNameConverter
		implements AttributeConverter<HostName, String> {

	@Override
	public String convertToDatabaseColumn(HostName value) {
		if (value == null) {
			return null;
		}
		return value.getName();
	}

	@Override
	public HostName convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new HostName(value);
	}

}
