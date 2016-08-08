package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.ScriptName;

@Converter
public class ScriptNameConverter
		implements AttributeConverter<ScriptName, String> {

	@Override
	public String convertToDatabaseColumn(ScriptName value) {
		if (value == null) {
			return null;
		}
		return value.getName();
	}

	@Override
	public ScriptName convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new ScriptName(value);
	}

}
