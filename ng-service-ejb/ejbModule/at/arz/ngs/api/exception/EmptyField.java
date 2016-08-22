package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

/**
 * This Exception will be thrown, if an required field is empty.
 * @author dani 
 *
 */

@ApplicationException(rollback = true)
public class EmptyField
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmptyField(String object) {
		super(object);
	}
}
