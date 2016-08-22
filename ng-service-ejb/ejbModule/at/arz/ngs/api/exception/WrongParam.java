package at.arz.ngs.api.exception;

import javax.ejb.ApplicationException;

/**
 * This exception will be thrown, if a wrong parameter is entered.
 * 
 * @author dani 
 *
 */
@ApplicationException
public class WrongParam
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WrongParam(String param) {
		super(param);
	}

}
