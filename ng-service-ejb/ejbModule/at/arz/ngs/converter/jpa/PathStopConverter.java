package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.PathStop;

@Converter
public class PathStopConverter
		implements AttributeConverter<PathStop, String> {

	@Override
	public String convertToDatabaseColumn(PathStop value) {
		if (value == null) {
			return null;
		}
		return value.getPath();
	}

	@Override
	public PathStop convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new PathStop(value);
	}

}
