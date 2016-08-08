package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.ServiceInstanceName;

@Converter
public class ServiceInstanceNameConverter
		implements AttributeConverter<ServiceInstanceName, String> {

	@Override
	public String convertToDatabaseColumn(ServiceInstanceName value) {
		if (value == null) {
			return null;
		}
		return value.getName();
	}

	@Override
	public ServiceInstanceName convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new ServiceInstanceName(value);
	}

}
