package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.ServiceName;

@Converter
public class ServiceNameConverter
		implements AttributeConverter<ServiceName, String> {

	@Override
	public String convertToDatabaseColumn(ServiceName value) {
		if (value == null) {
			return null;
		}
		return value.getName();
	}

	@Override
	public ServiceName convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new ServiceName(value);
	}

}
