package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

/**
 * This exception will be thrown, if the version of an entity changed during the GET and PUT process
 * 
 * @author rpci334
 *
 */
@ApplicationException(rollback = true)
public class AlreadyModified
		extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String object;

	public AlreadyModified(String object) {
		super(object + " wurde in der Zwischenzeit geändert!");
		this.object = object;
	}

	public String getReason() {
		return object + " wurde in der Zwischenzeit geändert!";
	}

}
