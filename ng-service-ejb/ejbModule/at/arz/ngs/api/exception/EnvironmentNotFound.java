package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.EnvironmentName;

@ApplicationException(rollback = true)
public class EnvironmentNotFound
		extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private EnvironmentName environmentName;

	public EnvironmentNotFound(EnvironmentName environmentName) {
		super(environmentName.toString());
		this.environmentName = environmentName;
	}

	public EnvironmentName getEnvironmentName() {
		return environmentName;
	}

}
