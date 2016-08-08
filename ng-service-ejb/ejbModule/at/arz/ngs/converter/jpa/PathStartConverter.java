package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.PathStart;

@Converter
public class PathStartConverter
		implements AttributeConverter<PathStart, String> {

	@Override
	public String convertToDatabaseColumn(PathStart value) {
		if (value == null) {
			return null;
		}
		return value.getPath();
	}

	@Override
	public PathStart convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new PathStart(value);
	}

}
