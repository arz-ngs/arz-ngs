package at.arz.ngs.converter.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import at.arz.ngs.api.JobId;

@Converter
public class JobIdConverter
		implements AttributeConverter<JobId, String> {

	@Override
	public String convertToDatabaseColumn(JobId value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	@Override
	public JobId convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return new JobId(value);
	}
}
