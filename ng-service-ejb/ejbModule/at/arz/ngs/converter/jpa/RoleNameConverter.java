package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.RoleName;

@Converter
public class RoleNameConverter implements AttributeConverter<RoleName, String>{

	@Override
	public String convertToDatabaseColumn(RoleName value) {
		if(value == null) {
			return null;
		}
		
		return value.getName();
	}

	@Override
	public RoleName convertToEntityAttribute(String value) {
		if(value == null) {
			return null;
		}
		return new RoleName(value);
	}

}
