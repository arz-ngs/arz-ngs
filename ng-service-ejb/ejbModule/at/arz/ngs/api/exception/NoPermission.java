package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

/**
 * This exception will be thrown, if the user has no permission to do this action.
 * 
 * @author dani 
 *
 */
@ApplicationException(rollback = true)
public class NoPermission
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoPermission(String reason) {
		super(reason);
	}

}
