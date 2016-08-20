package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.UserName;

@Converter
public class UserNameConverter implements AttributeConverter<UserName, String>{

	@Override
	public String convertToDatabaseColumn(UserName value) {
		if (value == null) {
			return null;
		}
		return value.getName();
	}

	@Override
	public UserName convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new UserName(value);
	}

}
