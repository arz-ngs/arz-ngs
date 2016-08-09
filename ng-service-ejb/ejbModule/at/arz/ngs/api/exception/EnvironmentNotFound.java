package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

import at.arz.ngs.api.EnvironmentName;

/**
 * This exception will be thrown, if an expected Environment wasn't found in the database.
 * 
 * @author rpci334
 *
 */

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
