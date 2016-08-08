package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.PathRestart;

@Converter
public class PathRestartConverter
		implements AttributeConverter<PathRestart, String> {

	@Override
	public String convertToDatabaseColumn(PathRestart value) {
		if (value == null) {
			return null;
		}
		return value.getPath();
	}

	@Override
	public PathRestart convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new PathRestart(value);
	}

}
