package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.PathStatus;

@Converter
public class PathStatusConverter
		implements AttributeConverter<PathStatus, String> {

	@Override
	public String convertToDatabaseColumn(PathStatus value) {
		if (value == null) {
			return null;
		}
		return value.getPath();
	}

	@Override
	public PathStatus convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new PathStatus(value);
	}

}
